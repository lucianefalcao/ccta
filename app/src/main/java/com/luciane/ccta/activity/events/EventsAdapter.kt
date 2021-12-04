package com.luciane.ccta.activity.events

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.R
import com.luciane.ccta.model.Event
import com.luciane.ccta.utils.DateTimeFormatter

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var events = listOf<Event>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_event, parent, false)

        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        val hour = holder.itemView.findViewById<TextView>(R.id.event_hour)
        val title = holder.itemView.findViewById<TextView>(R.id.event_title)
        val description = holder.itemView.findViewById<TextView>(R.id.event_description)

        hour.text = DateTimeFormatter.getTimeFromMilliseconds(event.lastModified)
        title.text = event.title
        description.text = event.description
    }

    override fun getItemCount(): Int {
        return events.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setEvents(newEvents : List<Event>){
        events = newEvents
        notifyDataSetChanged()
    }

    class EventViewHolder internal constructor(view : View) : RecyclerView.ViewHolder(view)
}