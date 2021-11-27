package com.luciane.ccta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import com.luciane.ccta.activity.chat.ChatActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<ImageButton>(R.id.sobreButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, AboutCenterActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.eventosButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.editaisButtonUserInputHome).setOnClickListener {

        }

        findViewById<ImageButton>(R.id.chatButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

    }
}