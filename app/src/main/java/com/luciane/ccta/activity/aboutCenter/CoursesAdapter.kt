package com.luciane.ccta.activity.aboutCenter
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luciane.ccta.databinding.CourseItemBinding
import com.luciane.ccta.model.Course

class CoursesAdapter (val courseList : ArrayList<Course>, val context: Context) :
    RecyclerView.Adapter<CoursesAdapter.MyCourses>() {

    class MyCourses(val binding: CourseItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCourses {
        val binding = CourseItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyCourses(binding)
    }

    override fun onBindViewHolder(holder: MyCourses, position: Int){
        val course = courseList[position]
        with(holder) {
            ("- " +course.name).also { binding.courseTitle.text = it }
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

//    private fun newsDetail(noticiaId: String){
//        //Log.d(NoticiasActivity.TAG, "Notícia: $noticiaId")
//        val intent = Intent(context, NewsDetailsActivity::class.java)
//        intent.putExtra("noticiaId", noticiaId)
//        context.startActivity(intent)
//    }
}
