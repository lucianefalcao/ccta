package com.luciane.ccta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.luciane.ccta.model.Turno

class DetalhesCursoActivity : AppCompatActivity() {

    companion object{
        val TAG = "DetalhesCursoScreen"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_curso)

        val courseId = intent.getStringExtra("cursoId")
        Log.d(TAG, "Course: $courseId")

        if(courseId == null)
            finish()

        listenForCourseChanges(courseId!!)
    }

    private fun listenForCourseChanges(courseId: String){
        FirebaseFirestore.getInstance().collection("courses").document(courseId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w(TAG, "Error on fetch data: ", error)
                    return@addSnapshotListener
                }

                if (value != null) {
                    val name = value.getString("name")!!
                    val subType = value.getString("subType")!!
                    val type = value.getString("type")!!
                    val nrPeriods = value.getString("nrPeriods")!!
                    val turno = value.getString("turno")!!
                    val description = value.getString("description")!!

                    findViewById<TextView>(R.id.textViewCourseName).text = name
                    val typeSubtype = "$type • $subType"
                    findViewById<TextView>(R.id.textViewType).text = typeSubtype
                    findViewById<TextView>(R.id.textViewDescricao).text = description

                    val turnoText = when (turno) {
                        Turno.M.toString() -> "Manhã"
                        Turno.I.toString() -> "Integral"
                        Turno.N.toString() -> "Noite"
                        Turno.T.toString() -> "Tarde"
                        else -> "Indefinido"
                    }

                    val turnoPeriodo = " ($turnoText • $nrPeriods períodos)"

                    findViewById<TextView>(R.id.textViewTurnoPeriodo).text = turnoPeriodo



                }
            }
    }
}