package com.luciane.ccta

import android.app.DownloadManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.activity.chat.ChatActivity
import com.luciane.ccta.activity.editais.EditaisActivity
import com.luciane.ccta.activity.editais.EditaisAdapter
import com.luciane.ccta.model.Edital

class HomeActivity : AppCompatActivity() {
    companion object{
        val TAG = "HomeScreen"
    }

    private var recyclerViewEditais: RecyclerView? = null
    private var editaisAdapter = EditaisAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        findViewById<ImageButton>(R.id.sobreButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, AboutCenterActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.eventosButtonUserInputHome).setOnClickListener {

        }

        findViewById<ImageButton>(R.id.editaisButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, EditaisActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.chatButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        editaisAdapter.downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        recyclerViewEditais = findViewById(R.id.recyclerViewEditaisHome)
        recyclerViewEditais!!.setAdapter(editaisAdapter)
        recyclerViewEditais!!.layoutManager = layoutManager
        listenForEditais()

    }

    private fun listenForEditais(){
        val ref = FirebaseFirestore.getInstance().collection("editais")
            .orderBy("lastModified").limitToLast(3)
        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Error on fetch data: ", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val editais = mutableListOf<Edital>()
                for (doc in snapshot) {
                    val id = doc.id
                    val title = doc.getString("title")!!
                    val documentPath = doc.getString("documentPath")!!
                    val userUid = doc.getString("userUid")!!
                    val lastModified = doc.getLong("lastModified")!!
                    editais.add(Edital(id, title, documentPath, userUid, lastModified))
                }
                editaisAdapter.setEditaisList(editais)
            }
        }
    }
}