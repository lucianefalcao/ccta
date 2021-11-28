package com.luciane.ccta.activity.editais

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.R
import com.luciane.ccta.model.Edital

class EditaisAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var editaisList: MutableList<Edital> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_edital_item, parent, false)

        return EditalItem(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val edital = this.editaisList[position]
        val title = edital.title
        val lastModifiedDateTime = edital.getFormatedLastModifiedDateTime()

        val viewItem = holder as EditalItem
        viewItem.itemView.findViewById<TextView>(R.id.textViewTitleEditalItem).text = title
        viewItem.itemView
            .findViewById<TextView>(R.id.textViewLastModifiedEditalItem).text = lastModifiedDateTime
    }

    override fun getItemCount(): Int {
        return editaisList.size
    }

    fun setEditaisList(editaisList: MutableList<Edital>) {
        this.editaisList = editaisList
        notifyDataSetChanged()
    }

    class EditalItem internal constructor(view: View) : RecyclerView.ViewHolder(view)
}