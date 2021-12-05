package com.luciane.ccta

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.activity.editais.EditaisAdapter
import com.luciane.ccta.activity.noticias.DetalhesNoticiaActivity
import com.luciane.ccta.activity.noticias.NoticiasActivity
import com.luciane.ccta.model.Course
import com.luciane.ccta.model.Edital
import com.luciane.ccta.utils.DPDimensionConverter

class AboutCenterActivity : AppCompatActivity(){
    companion object{
        val TAG = "AboutCenterActivity"
    }

    private var recyclerViewCourse: RecyclerView? = null
    private var aboutCenterAdapter = AboutCenterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.about_center)

        aboutCenterAdapter.onItemClick = { courseId -> detalhesCurso(courseId) }
        recyclerViewCourse = findViewById(R.id.recyclerViewCursos)
        Log.d(TAG, recyclerViewCourse.toString())
        recyclerViewCourse!!.setAdapter(aboutCenterAdapter)

        getCenterInfo()

        val mapImage = findViewById<ImageView>(R.id.ccta_map_image)
        mapImage.setOnClickListener { view ->
            startCctaMap()
        }

        listenForCourses()
    }

    fun startCctaMap(){
        val labelCcta = Uri.encode("Centro De Comunicação, Turismo e Artes - CCTA/UFPB - Jardim Cidade Universitária, João Pessoa - PB")
        val mapIntentUri = Uri.parse("geo:7.1374775705513205,-34.849560968837274?q=$labelCcta&z=21")

        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")

        startActivity(mapIntent)
    }

    private fun getCenterInfo(){
        val ref = FirebaseFirestore.getInstance().collection("center").get().addOnSuccessListener {
            var description = ""
            for (doc in it.documents) {
                description = doc.getString("about")!!
            }

            val aboutText = findViewById<TextView>(R.id.about_center)
            aboutText.text = description
        }
    }

    private fun listenForCourses(){
        val ref = FirebaseFirestore.getInstance().collection("courses")
            .orderBy("name")
        ref.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w(AboutCenterActivity.TAG, "Error on fetch data: ", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val courses = mutableListOf<Course>()
                for (doc in snapshot) {
                    val id = doc.id
                    val name = doc.getString("name")!!
                    val subType = doc.getString("subType")!!
                    val type = doc.getString("type")!!
                    val nrPeriods = doc.getString("nrPeriods")!!
                    val turno = doc.getString("turno")!!
                    val description = doc.getString("description")!!
                    val lastModified = doc.getLong("lastModified")!!
                    val userUid = doc.getString("userUid")!!
                    courses.add(Course(id, name, subType, type, nrPeriods, description, turno, lastModified, userUid))
                }
                aboutCenterAdapter.setCourseList(courses)
            }

        }
    }

    private fun detalhesCurso(cursoId: String){
        Log.d(AboutCenterActivity.TAG, "Curso: $cursoId")
        val intent = Intent(this, DetalhesCursoActivity::class.java)
        intent.putExtra("cursoId", cursoId)
        startActivity(intent)
    }
}