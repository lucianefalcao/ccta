package com.luciane.ccta.activity.events

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.R
import com.luciane.ccta.model.Event
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class EventsActivity : AppCompatActivity() {

    companion object{
        val WEEK_DAYS = Arrays.asList(
            "Seg",
            "Ter",
            "Qua",
            "Qui",
            "Sex",
            "Sab",
            "Dom"
        )
    }

    private var selectedDay = LocalDateTime.now()
    private var currentButtonDay = 0
    private var totalEvents = listOf<Event>()
    private var selectedEvents = listOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        val eventsAdapter = EventsAdapter()
        val recycleView = findViewById<RecyclerView>(R.id.event_recycle_view)

        recycleView.adapter = eventsAdapter

        val temp = LocalDateTime.now()

        val startDate = LocalDateTime.of(temp.year, temp.month, temp.dayOfMonth, 0, 0)
        val endDate = startDate.plusDays(7)

        val start = startDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000
        val end = endDate.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

        val ref = FirebaseFirestore.getInstance()
            .collection("events")
            .whereGreaterThanOrEqualTo("date", start)
            .whereLessThanOrEqualTo("date", end)
            .orderBy("date")
            .orderBy("lastModified")

        ref.addSnapshotListener { snapshot, error ->

            if(error != null){
                return@addSnapshotListener
            }

            if(snapshot != null){

                totalEvents = snapshot
                    .map { data -> Event(
                        data.getLong("date")!!,
                        data.getString("description")!!,
                        data.getLong("lastModified")!!,
                        data.getString("title")!!,
                        data.getString("userUid")!!
                    )
                }

                selectEvents(eventsAdapter)
            }
        }

        val buttons = Arrays.asList(
            findViewById<AppCompatButton>(R.id.event_button0),
            findViewById<AppCompatButton>(R.id.event_button1),
            findViewById<AppCompatButton>(R.id.event_button2),
            findViewById<AppCompatButton>(R.id.event_button3),
            findViewById<AppCompatButton>(R.id.event_button4),
            findViewById<AppCompatButton>(R.id.event_button5),
            findViewById<AppCompatButton>(R.id.event_button6)
        )

        val dayLabel = findViewById<TextView>(R.id.event_day_label)

        val today = LocalDateTime.now()

        buttons[0].setBackgroundColor(Color.WHITE)

        for(index in 0..(buttons.size - 1)){
            val button = buttons[index]
            val currentDay = today.plusDays(index.toLong())
            button.text = WEEK_DAYS[currentDay.dayOfWeek.value - 1] + "\n${currentDay.dayOfMonth}"
            button.setOnClickListener{
                selectedDay = currentDay
                button.setBackgroundColor(Color.WHITE)
                //troca o botão clicado
                dayLabel.text = when(index){
                    0 -> "Hoje"
                    1 -> "Amanhã"
                    else -> WEEK_DAYS[currentDay.dayOfWeek.value - 1]
                }
                buttons[currentButtonDay].setBackgroundResource(R.drawable.rounded_button_date)
                buttons[index].setBackgroundColor(Color.WHITE)
                currentButtonDay = index
                selectEvents(eventsAdapter)
            }
        }
    }

    private fun selectEvents(adapter : EventsAdapter){
        selectedEvents = totalEvents.filter { data ->
            val timestamp = data.date

            val dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                ZoneId.systemDefault()
            )

            dateTime.year == selectedDay.year &&
            dateTime.month == selectedDay.month &&
            dateTime.dayOfMonth == selectedDay.dayOfMonth
        }

        adapter.setEvents(selectedEvents)
    }
}