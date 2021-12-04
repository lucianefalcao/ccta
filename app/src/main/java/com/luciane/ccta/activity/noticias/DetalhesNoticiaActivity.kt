package com.luciane.ccta.activity.noticias

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.luciane.ccta.R
import com.luciane.ccta.utils.DateTimeFormatter
import com.squareup.picasso.Picasso

class DetalhesNoticiaActivity : AppCompatActivity() {
    companion object{
        val TAG = "DetalhesNoticiaScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_noticia)

        val noticiaId = intent.getStringExtra("noticiaId")
        Log.d(TAG, "Notícia: $noticiaId")

        if(noticiaId == null)
            finish()

        listenForNoticiaChanges(noticiaId!!)
    }

    private fun listenForNoticiaChanges(noticiaId: String){
        FirebaseFirestore.getInstance().collection("news").document(noticiaId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Error on fetch data: ", error)
                    return@addSnapshotListener
                }

                if (value != null && value.getString("state")!! == "published") {
                    val title = value.getString("title")!!
                    val newsText = value.getString("newsText")!!
                    val coverPath = value.getString("coverPath")!!
                    val userUid = value.getString("userUid")!!
                    val lastModified = value.getLong("lastModified")!!

                    FirebaseStorage.getInstance().reference.child(coverPath)
                        .downloadUrl.addOnSuccessListener {
                            val noticiaImageView = findViewById<ImageView>(R.id.imageViewDetalhesNoticia)
                                Picasso.get().load(it).into(noticiaImageView)
                        }.addOnFailureListener {
                            TODO("Not yet implemented")
                        }

                    findViewById<TextView>(R.id.textViewTitleDetalheNoticia).text = title
                    findViewById<TextView>(R.id.textViewLastModifiedDetalheNoticia)
                        .text = DateTimeFormatter.getFormatedDateTimeFromMilliseconds(lastModified)

                    findViewById<TextView>(R.id.textViewContentDetalheNoticia).text =
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                            Html.fromHtml(newsText, Html.FROM_HTML_MODE_COMPACT)
                        } else{
                            Html.fromHtml(newsText)
                        }

                }
            }
    }
}