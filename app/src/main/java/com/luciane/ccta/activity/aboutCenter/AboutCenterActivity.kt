package com.luciane.ccta.activity.aboutCenter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.R
import com.luciane.ccta.databinding.AboutCenterBinding
import com.luciane.ccta.model.Course

class AboutCenterActivity : AppCompatActivity(){
    lateinit var db: FirebaseFirestore
    lateinit var binding: AboutCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_center)
        db = FirebaseFirestore.getInstance()
        binding = AboutCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadAllData()
        val mapImage = findViewById<ImageView>(R.id.ccta_map_image)
        mapImage.setOnClickListener { view ->
            startCctaMap()
        }
    }
    private fun loadAllData() {
        val coursesList = ArrayList<Course>()
        val ref = db.collection("courses")
        ref.get().addOnSuccessListener {
            if (it.isEmpty){
                Toast.makeText(this@AboutCenterActivity, "Ops, parece que não foi possivel carregar os cursos"
                    , Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            for (course in it) {
                val courseModel = course.toObject(Course::class.java)
                coursesList.add(courseModel)
            }
            binding.coursesTitleList.apply {
                layoutManager = LinearLayoutManager(this@AboutCenterActivity, RecyclerView.VERTICAL, false)
                adapter = CoursesAdapter(coursesList, this@AboutCenterActivity)
            }
        }
    }
    fun startCctaMap(){
        val labelCcta = Uri.encode("Centro De Comunicação, Turismo e Artes - CCTA/UFPB - Jardim Cidade Universitária, João Pessoa - PB")
        val mapIntentUri = Uri.parse("geo:7.1374775705513205,-34.849560968837274?q=$labelCcta&z=21")

        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        startActivity(mapIntent)
    }
}