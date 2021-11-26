package com.luciane.ccta

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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

        val register = findViewById<Button>(R.id.button)
        val et_register_email = findViewById<TextInputEditText>(R.id.email)
        val et_register_password = findViewById<TextInputEditText>(R.id.senha)

        register.setOnClickListener {
            when{
                TextUtils.isEmpty(et_register_email.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informa teu e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informa tua senha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = et_register_email.text.toString().trim{it <= ' '}
                    val senha: String = et_register_password.text.toString().trim{it <= ' '}

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->

                                if(task.isSuccessful){
                                    val firebaseUser: FirebaseUser = task.result!!.user!!
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        "Você se cadastrou com sucesso.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent = Intent(this@RegistrationActivity, AboutCenterActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                }
                                else{
                                    Toast.makeText(
                                        this@RegistrationActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        )
                }
            }
        }
    }
}