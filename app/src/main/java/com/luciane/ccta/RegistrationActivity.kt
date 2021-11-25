package com.luciane.ccta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val termsOfUse = findViewById<TextView>(R.id.textView9)

        termsOfUse.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
            finish()
        }

        val privacityPolitics = findViewById<TextView>(R.id.textView11)

        privacityPolitics.setOnClickListener {
            startActivity(Intent(this, TermsActivity::class.java))
            finish()
        }
    }
}