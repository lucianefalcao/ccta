package com.luciane.ccta

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ClipData.Item
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.luciane.ccta.activity.chat.ChatActivity
import com.luciane.ccta.model.User


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userInfo = getIntent();
        val user = userInfo.getParcelableExtra<User>("User");
        val login = user?.login;
        val password = user?.password;

        val viewLogin = findViewById<TextView>(R.id.viewLogin) as TextView
        viewLogin.text = "Login: "+login

        val viewPassword = findViewById<TextView>(R.id.viewPassword) as TextView
        viewPassword.text = "Senha: "+password

        val chatButton = findViewById<Button>(R.id.chatButton)
        chatButton.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

    }
}