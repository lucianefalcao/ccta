package com.luciane.ccta.activity.chat

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ImageButton
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.luciane.ccta.R
import com.luciane.ccta.model.ChatMessage
import android.content.SharedPreferences
import com.luciane.ccta.utils.DateTimeFormatter

class ChatActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatScreen"
    }

    private var recyclerViewChatLog: RecyclerView? = null
    private var chatAdapter = ChatAdapter()

    private var name: String? = null
    private var fromId: String? = null
    private val toId = "admin"

    private val KEY_UID = "com.luciane.ccta.UID"
    private val KEY_NAME = "com.luciane.ccta.NAME"
    private val USER_PREFS = "USER_PREFS"
    private var userSharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerViewChatLog = findViewById(R.id.recyclerViewChat)
        recyclerViewChatLog!!.setAdapter(chatAdapter)

        findViewById<ImageButton>(R.id.sendButtonUserInputChat).setOnClickListener {
            performSendMessage()
        }

        checkUserIdentity()
    }

    private fun listenForMessages(){
        val fromId = this.fromId
        val toId = toId

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d(TAG, chatMessage?.text!!)
                if(chatMessage.fromId == fromId){
                    chatMessage.type = ChatMessage.MessageType.RIGHT
                } else{
                    chatMessage.type = ChatMessage.MessageType.LEFT
                }
                chatAdapter.addMessage(chatMessage)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun performSendMessage(){
        val editText = findViewById<EditText>(R.id.editTextUserInputChat)
        val text = editText.text.toString()

        val sendAtDateTime = DateTimeFormatter.getCurrentFormatedDateTimeDayMonthYearHourMinute()

        val fromId = this.fromId!!
        val toId = this.toId

        val refFrom = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$fromId/$toId").push()
        val refTo = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(refFrom.key!!, text, fromId, toId, sendAtDateTime)

        refFrom.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${refFrom.key}")
                editText.text.clear()
                recyclerViewChatLog!!.scrollToPosition(chatAdapter.itemCount - 1)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to send message:\n${it.message}")
            }

        refTo.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${refTo.key}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to send message:\n${it.message}")
            }

        val refLatestMessageFrom = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$fromId/$toId")
        val refLatestMessageTo = FirebaseDatabase.getInstance()
            .getReference("/latest-messages/$toId/$fromId")

        refLatestMessageFrom.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Latest message saved: ${refLatestMessageFrom.key}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save latest message:\n${it.message}")
            }

        refLatestMessageTo.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Latest message saved: ${refLatestMessageTo.key}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to save latest message:\n${it.message}")
            }
    }

    private fun checkUserIdentity(){
        userSharedPreferences = getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE)
        name = userSharedPreferences!!.getString(KEY_NAME, null)
        if (name == null){
            showInputDialog()
        } else {
            fromId = userSharedPreferences!!.getString(KEY_UID, null)
            listenForMessages()
        }
    }

    private fun showInputDialog(){
        val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Qual é seu nome?")

        val input = EditText(this)
        input.setHint("Nome")
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setError("Digite seu nome")
        builder.setView(input)

        builder.setPositiveButton("Começar conversa",
            DialogInterface.OnClickListener { dialog, which ->
                if(input.text.toString().isNotBlank()){
                    saveUserPreferences(input.text.toString())
                } else {
                    finish()
                }
        })
        builder.setNegativeButton("Cancelar",
            DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()
                finish()
        })

        builder.show()
    }

    private fun saveUserPreferences(name: String){
        this.name = name
        val refFrom = FirebaseDatabase.getInstance().getReference("/user-messages").push()
        fromId = refFrom.key
        Log.d(TAG, "User uid generated successfully: $fromId")

        listenForMessages()

        val editor: SharedPreferences.Editor = userSharedPreferences!!.edit()
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_UID, fromId)

        editor.commit()

    }
}