package com.luciane.ccta.activity.chat

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
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
import android.os.Handler
import androidx.navigation.NavController
import com.google.android.gms.tasks.Task
import com.luciane.ccta.HomeActivity
import com.luciane.ccta.activity.noticias.NoticiasActivity
import com.luciane.ccta.model.Visitante
import com.luciane.ccta.utils.DateTimeFormatter

class ChatActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatScreen"
    }

    private var recyclerViewChatLog: RecyclerView? = null
    private var chatAdapter = ChatAdapter()

    private var name: String? = null
    private var chatId: String? = null
    private var fromId: String? = null

    private val KEY_UID = "com.luciane.ccta.UID"
    private val KEY_NAME = "com.luciane.ccta.NAME"
    private val KEY_CHAT = "com.luciane.ccta.CHAT"
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
        val ref = FirebaseDatabase.getInstance().getReference("/chats/$chatId/messages")
        val that = this
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d(TAG, chatMessage?.message!!)
                if(chatMessage.memberId == fromId){
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
                val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(that)
                builder.setTitle("Seu chat foi encerrado")
                builder.setMessage("você sempre pode abrir uma nova conversa!")
                builder.show()

                Handler().postDelayed(Runnable {
                    finish()
                }, 3000)
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

        val refMessages = FirebaseDatabase.getInstance()
            .getReference("/chats/$chatId/messages").push()

        val chatMessage = ChatMessage(fromId!!, text, fromId!!, DateTimeFormatter.getCurrentTimestamp())

        refMessages.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${refMessages.key}")
                editText.text.clear()
                recyclerViewChatLog!!.scrollToPosition(chatAdapter.itemCount - 1)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to send message:\n${it.message}")
            }
    }

    private fun checkUserIdentity(){
        userSharedPreferences = getSharedPreferences(USER_PREFS, Activity.MODE_PRIVATE)
        name = userSharedPreferences!!.getString(KEY_NAME, null)
        if (name == null){
            showInputDialog()
        } else {
            fromId = userSharedPreferences!!.getString(KEY_UID, null)
            chatId = userSharedPreferences!!.getString(KEY_CHAT, null)

            val chatRef = FirebaseDatabase.getInstance().getReference("/chats/$chatId")
            chatRef.get().addOnSuccessListener {
                if(it.exists()){
                    listenForMessages()
                } else {
                    saveUserPreferences(name!!)
                }
            }
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
        val refChat = FirebaseDatabase.getInstance().getReference("/chats").push()
        chatId = refChat.key

        val refVisitante = FirebaseDatabase.getInstance()
            .getReference("/chats/${refChat.key}/visitante").push()
        fromId = refVisitante.key
        val visitante = Visitante(fromId!!, name!!)
        refVisitante.setValue(visitante)
            .addOnSuccessListener {
                Log.d(TAG, "User saved successfully: $fromId")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to send message:\n${it.message}")
            }
        Log.d(TAG, "User uid generated successfully: $fromId")

        listenForMessages()

        val editor: SharedPreferences.Editor = userSharedPreferences!!.edit()
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_UID, fromId)
        editor.putString(KEY_CHAT, chatId)

        editor.commit()

    }
}