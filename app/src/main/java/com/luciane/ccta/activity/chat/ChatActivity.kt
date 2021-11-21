package com.luciane.ccta.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.luciane.ccta.R
import com.luciane.ccta.model.ChatMessage

class ChatActivity : AppCompatActivity() {
    companion object{
        val TAG = "ChatScreen"
        val FROM_ID = "testUser"
        val TO_ID = "admin"
    }

    var recyclerViewChatLog: RecyclerView? = null
    var chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        supportActionBar?.title = "Chat"

        recyclerViewChatLog = findViewById(R.id.recyclerViewChat)
        recyclerViewChatLog!!.setAdapter(chatAdapter)

        listenForMessages()

        findViewById<ImageButton>(R.id.sendButtonUserInputChat).setOnClickListener {
            performSendMessage()
        }
    }

    private fun listenForMessages(){
        val fromId = FROM_ID
        val toId = TO_ID

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)
                Log.d(TAG, chatMessage?.text!!)
                if(chatMessage.fromId == FROM_ID){
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

        val fromId = if(chatAdapter.itemCount % 2 == 0) FROM_ID else TO_ID
        val toId = if(chatAdapter.itemCount % 2 == 0) TO_ID else FROM_ID

        val refFrom = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$fromId/$toId").push()
        val refTo = FirebaseDatabase.getInstance()
            .getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(refFrom.key!!, text, fromId,
                                    toId, System.currentTimeMillis()/1000)

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
    }
}