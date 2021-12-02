package com.luciane.ccta.activity.noticias

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.R
import com.luciane.ccta.model.Noticia
import com.luciane.ccta.utils.DPDimensionConverter
import kotlin.math.roundToInt

class NoticiasActivity : AppCompatActivity() {
    companion object{
        val TAG = "NoticiasScreen"
    }

    private var recyclerViewNoticias: RecyclerView? = null
    private var noticiasAdapter = NoticiasAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticias)

        noticiasAdapter.layoutSize = DPDimensionConverter.dpToPx(resources.displayMetrics, 256f)
        noticiasAdapter.onItemClick = { noticiaId -> detalhesNoticia(noticiaId) }
        recyclerViewNoticias = findViewById(R.id.recyclerViewNoticias)
        recyclerViewNoticias!!.setAdapter(noticiasAdapter)

        listenForNoticias()
    }

    private fun listenForNoticias(){
        val ref = FirebaseFirestore.getInstance().collection("news")
            .whereEqualTo("state", "published").orderBy("lastModified")
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
        //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        //intent.putExtra("noticia", noticia)
        startActivity(intent)
    }
}