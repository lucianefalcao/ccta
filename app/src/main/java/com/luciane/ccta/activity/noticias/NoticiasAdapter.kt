package com.luciane.ccta.activity.noticias

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.luciane.ccta.R
import com.luciane.ccta.model.Noticia
import com.squareup.picasso.Picasso

class NoticiasAdapter(var onItemClick: ((Noticia) -> Unit)? = null)
    : RecyclerView.Adapter<NoticiasAdapter.NoticiaItem>() {

    private var noticiasList: MutableList<Noticia> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticiaItem {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_noticia_item, parent, false)

        return NoticiaItem(itemView)
    }

    override fun onBindViewHolder(holder: NoticiaItem, position: Int) {
        val noticia = this.noticiasList[position]
        val title = noticia.title

        holder.itemView.findViewById<TextView>(R.id.textViewNoticiaItem).text = title

        FirebaseStorage.getInstance().reference.child(noticia.coverPath)
            .downloadUrl.addOnSuccessListener {
                val targetImageView = holder.itemView.findViewById<ImageView>(R.id.imageViewNoticiaItem)
                Picasso.get().load(it).into(targetImageView)
            } .addOnFailureListener {
                TODO("Not yet implemented")
            }
    }

    override fun getItemCount(): Int {
        return noticiasList.size
    }

    fun setNoticiasList(noticiasList: MutableList<Noticia>) {
        this.noticiasList = noticiasList
        notifyDataSetChanged()
    }

    inner class NoticiaItem constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                onItemClick?.invoke(noticiasList[adapterPosition])
            }
        }
    }
}