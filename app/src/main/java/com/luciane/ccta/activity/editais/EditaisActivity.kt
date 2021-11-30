package com.luciane.ccta.activity.editais

import android.app.DownloadManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.R
import com.luciane.ccta.model.Edital

class EditaisActivity : AppCompatActivity() {
    companion object{
        val TAG = "EditaisScreen"
    }

    private var recyclerViewEditais: RecyclerView? = null
    private var editaisAdapter = EditaisAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editais)

        editaisAdapter.downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        recyclerViewEditais = findViewById(R.id.recyclerViewEditais)
        recyclerViewEditais!!.setAdapter(editaisAdapter)

        listenForEditais()
    }

    private fun listenForEditais(){
        val ref = FirebaseFirestore.getInstance().collection("editais").orderBy("lastModified")
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