package com.luciane.ccta

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.model.Course

class AboutCenterAdapter(var layoutSize: Int = 0, var onItemClick: ((String) -> Unit)? = null)
    : RecyclerView.Adapter<AboutCenterAdapter.CourseItem>() {

    private var courseList: MutableList<Course> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItem {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_course_item, parent, false)

        return CourseItem(itemView)
    }

    override fun onBindViewHolder(holder: CourseItem, position: Int) {
        val course = courseList[position]

        holder.itemView.findViewById<TextView>(R.id.textViewCourseItem).text = course.name
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCourseList(courseList: MutableList<Course>) {
        this.courseList = courseList
        notifyDataSetChanged()
    }

    inner class CourseItem constructor(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                onItemClick?.invoke(courseList[adapterPosition].id)
            }
        }
    }
}