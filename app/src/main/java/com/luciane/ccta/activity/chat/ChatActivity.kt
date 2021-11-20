package com.luciane.ccta.activity.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.R
import com.luciane.ccta.model.ChatMessage

class ChatActivity : AppCompatActivity() {
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
        val chatMessage1 = ChatMessage("", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod.",
            "", "", System.currentTimeMillis()/1000,
            ChatMessage.MessageType.RIGHT)
        val chatMessage2 = ChatMessage("", "Lorem ipsum dolor sit amet, consectetur adipiscing elit?",
            "", "", System.currentTimeMillis()/1000,
            ChatMessage.MessageType.LEFT)
        val chatMessage3 = ChatMessage("", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
            "", "", System.currentTimeMillis()/1000,
            ChatMessage.MessageType.RIGHT)
        val chatMessage4 = ChatMessage("", "Lorem ipsum dolor sit amet, consectetur adipiscing elit?",
            "", "", System.currentTimeMillis()/1000,
            ChatMessage.MessageType.LEFT)

        chatAdapter.addMessage(chatMessage1)
        chatAdapter.addMessage(chatMessage2)
        chatAdapter.addMessage(chatMessage3)
        chatAdapter.addMessage(chatMessage4)
    }

    private fun performSendMessage(){
        val text = findViewById<EditText>(R.id.editTextUserInputChat).text.toString()

        val chatMessage = ChatMessage("", text, "",
                                    "", System.currentTimeMillis()/1000)

        if(chatAdapter.itemCount % 2 == 0){
            chatMessage.type = ChatMessage.MessageType.RIGHT
        } else{
            chatMessage.type = ChatMessage.MessageType.LEFT
        }

        chatAdapter.addMessage(chatMessage)
    }
}