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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.model.Usuario

class RegistrationActivity : AppCompatActivity() {

    private fun salvarUsuario(nome: String, email: String, endereco: String, telefone: String, senha: String){
        val db = FirebaseFirestore.getInstance()

        val ref = db.collection("users")

        val usuario = Usuario(nome, email, endereco, telefone, senha)
        ref.document(email).set(usuario)
    }

    private fun autenticarUsuario(email: String, senha: String) {
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
        val et_register_nome = findViewById<TextInputEditText>(R.id.nome)
        val et_register_email = findViewById<TextInputEditText>(R.id.email)
        val et_register_endereco = findViewById<TextInputEditText>(R.id.endereco)
        val et_register_telefone = findViewById<TextInputEditText>(R.id.telefone)
        val et_register_password = findViewById<TextInputEditText>(R.id.senha)

        register.setOnClickListener {
            when{
                TextUtils.isEmpty(et_register_nome.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informe seu nome",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_email.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informe seu e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_endereco.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informe seu endereço",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_telefone.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informe seu telefone",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                TextUtils.isEmpty(et_register_password.text.toString().trim{it <= ' '}) -> {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Informe sua senha",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val nome: String = et_register_nome.text.toString().trim{it <= ' '}
                    val email: String = et_register_email.text.toString().trim{it <= ' '}
                    val endereco: String = et_register_endereco.text.toString().trim{it <= ' '}
                    val telefone: String = et_register_telefone.text.toString().trim{it <= ' '}
                    val senha: String = et_register_password.text.toString().trim{it <= ' '}

                    salvarUsuario(nome, email, endereco, telefone, senha)
                    autenticarUsuario(email, senha)
                }
            }
        }
    }
}