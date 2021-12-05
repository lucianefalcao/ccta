package com.luciane.ccta.activity.noticias

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        val buttonShare = findViewById<ImageButton>(R.id.btnShare)

        buttonShare.setOnClickListener {
            FirebaseFirestore.getInstance().collection("news").document(noticiaId).addSnapshotListener{ value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (value != null && value.getString("state")!! == "published") {
                    val title = value.getString("title")!!
                    //val newsText = value.getString("newsText")!!
                    val linkApp = "https://play.google.com/store?hl=pt_BR&gl=US"

                    val sendIntent = Intent(Intent.ACTION_SEND)
                    sendIntent.type = "text/*"
                    sendIntent.putExtra(Intent.EXTRA_TEXT, title + "...\n" +
                            "\n(Para vizualizar a notícia completa baixe o app do CCTA $linkApp)")
                    val shareIntent = Intent.createChooser(sendIntent, "enviando noticia")
                    startActivity(shareIntent)

                } else {
                    Log.d(TAG, "Current data: null")
                }
            }
        }
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