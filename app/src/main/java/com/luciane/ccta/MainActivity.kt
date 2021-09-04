package com.luciane.ccta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.luciane.ccta.model.User

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginText = findViewById<EditText>(R.id.editTextTextPersonName)
        val passwordText = findViewById<EditText>(R.id.editTextTextPassword)
        val buttonLogin = findViewById<Button>(R.id.button)

        buttonLogin.setOnClickListener {
            val userInfo = Intent(this, HomeActivity::class.java)

            val user = User(loginText.text.toString(), passwordText.text.toString())
            userInfo.putExtra("User", user);
            startActivity(userInfo)
        }
    }
}