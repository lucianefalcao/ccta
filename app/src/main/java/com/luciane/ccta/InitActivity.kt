package com.luciane.ccta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        val buttonInit = findViewById<Button>(R.id.button)

        buttonInit.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        val login = findViewById<TextView>(R.id.textView5)

        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val register = findViewById<TextView>(R.id.textView6)

        register.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }
    }
}