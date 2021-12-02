package com.luciane.ccta.activity.noticias

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.R
import com.luciane.ccta.model.Noticia

class NoticiasActivity : AppCompatActivity() {
    companion object{
        val TAG = "NoticiasScreen"
    }

    private var recyclerViewNoticias: RecyclerView? = null
    private var noticiasAdapter = NoticiasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)

        noticiasAdapter.onItemClick = { noticia -> detalhesNoticia(noticia) }
        recyclerViewNoticias = findViewById(R.id.recyclerViewNoticias)
        recyclerViewNoticias!!.setAdapter(noticiasAdapter)

        listenForNoticias()
    }

    private fun listenForNoticias(){
        val ref = FirebaseFirestore.getInstance().collection("news").orderBy("lastModified")
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
                    val newsText = doc.getString("newsText")!!
                    val state = doc.getString("state")!!
                    val coverPath = doc.getString("coverPath")!!
                    val userUid = doc.getString("userUid")!!
                    val lastModified = doc.getLong("lastModified")!!
                    noticias.add(Noticia(id, title, newsText, state, coverPath, userUid, lastModified))
                }
                noticiasAdapter.setNoticiasList(noticias)
            }
        }
    }

    private fun detalhesNoticia(noticia: Noticia){
        Log.d(TAG, "Notícia: ${noticia.title}")
    }
}