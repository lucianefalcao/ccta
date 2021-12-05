package com.luciane.ccta

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.activity.chat.ChatActivity
import com.luciane.ccta.activity.editais.EditaisActivity
import com.luciane.ccta.activity.editais.EditaisAdapter
import com.luciane.ccta.activity.events.EventsActivity
import com.luciane.ccta.activity.noticias.DetalhesNoticiaActivity
import com.luciane.ccta.activity.noticias.NoticiasActivity
import com.luciane.ccta.activity.noticias.NoticiasAdapter
import com.luciane.ccta.AboutCenterActivity
import com.luciane.ccta.model.Edital
import com.luciane.ccta.model.Noticia
import com.luciane.ccta.utils.DPDimensionConverter

class HomeActivity : AppCompatActivity() {
    companion object{
        val TAG = "HomeScreen"
    }

    private var recyclerViewEditais: RecyclerView? = null
    private var editaisAdapter = EditaisAdapter()

    private var recyclerViewNoticias: RecyclerView? = null
    private var noticiasAdapter = NoticiasAdapter()

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
            val intent = Intent(this, EditaisActivity::class.java)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.chatButtonUserInputHome).setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.textViewVerMaisNoticiasHome).setOnClickListener {
            val intent = Intent(this, NoticiasActivity::class.java)
            startActivity(intent)
        }

        val editaisLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        editaisAdapter.downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        recyclerViewEditais = findViewById(R.id.recyclerViewEditaisHome)
        recyclerViewEditais!!.setAdapter(editaisAdapter)
        recyclerViewEditais!!.layoutManager = editaisLayoutManager
        listenForEditais()

        val noticiasLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL ,false)
        noticiasAdapter.layoutSize = DPDimensionConverter.dpToPx(resources.displayMetrics, 264f)
        noticiasAdapter.onItemClick = { noticiaId -> detalhesNoticia(noticiaId) }
        recyclerViewNoticias = findViewById(R.id.recyclerViewNoticiasHome)
        recyclerViewNoticias!!.setAdapter(noticiasAdapter)
        recyclerViewNoticias!!.layoutManager = noticiasLayoutManager
        listenForNoticias()

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

    private fun listenForNoticias(){
        val ref = FirebaseFirestore.getInstance().collection("news")
            .whereEqualTo("state", "published").orderBy("lastModified")
            .limitToLast(3)
        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(TAG, "Error on fetch data: ", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val noticias = mutableListOf<Noticia>()
                for (doc in snapshot) {
                    val id = doc.id
                    val title = doc.getString("title")!!
                    val coverPath = doc.getString("coverPath")!!
                    noticias.add(Noticia(id, title, coverPath))
                }
                noticiasAdapter.setNoticiasList(noticias)
            }
        }
    }

    private fun detalhesNoticia(noticiaId: String){
        Log.d(TAG, "Notícia: $noticiaId")
        val intent = Intent(this, DetalhesNoticiaActivity::class.java)
        intent.putExtra("noticiaId", noticiaId)
        startActivity(intent)
    }
}