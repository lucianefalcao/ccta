package com.luciane.ccta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val buttonInit = findViewById<Button>(R.id.button)

        buttonInit.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        val forgotPassword = findViewById<TextView>(R.id.textView6)

        forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }
    }
}