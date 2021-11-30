package com.luciane.ccta.activity.editais

import android.app.DownloadManager
import android.os.Build
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.luciane.ccta.R
import com.luciane.ccta.model.Edital

class EditaisAdapter(var downloadManager: DownloadManager? = null)
    : RecyclerView.Adapter<EditaisAdapter.EditalItem>() {

    private var editaisList: MutableList<Edital> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditalItem {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_edital_item, parent, false)

        return EditalItem(itemView)
    }

    override fun onBindViewHolder(holder: EditalItem, position: Int) {
        val edital = this.editaisList[position]
        val title = edital.title
        val lastModifiedDateTime = edital.getFormatedLastModifiedDateTime()

        holder.itemView.findViewById<TextView>(R.id.textViewTitleEditalItem).text = title
        holder.itemView
            .findViewById<TextView>(R.id.textViewLastModifiedEditalItem).text = lastModifiedDateTime
    }

    override fun getItemCount(): Int {
        return editaisList.size
    }

    fun setEditaisList(editaisList: MutableList<Edital>) {
        this.editaisList = editaisList
        notifyDataSetChanged()
    }

    private fun downloadEditalFromURL(documentPath: String){
        if(downloadManager != null) {
            FirebaseStorage.getInstance().reference.child(documentPath)
                .downloadUrl.addOnSuccessListener {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val filename = documentPath.substring(8)
                        val request = DownloadManager.Request(it)
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setDestinationInExternalPublicDir(
                            Environment.DIRECTORY_DOWNLOADS,
                            filename
                        )
                        val reference = downloadManager!!.enqueue(request)
                    } else {
                        TODO("VERSION.SDK_INT < M")
                    }
                }.addOnFailureListener {
                    // Handle any errors
                }
        }
    }

    inner class EditalItem constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                downloadEditalFromURL(editaisList[adapterPosition].documentPath)
            }
        }
    }
}