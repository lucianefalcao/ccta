package com.luciane.ccta.activity.news

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.luciane.ccta.databinding.ActivityNewsBinding
import com.luciane.ccta.model.News

class NewsActivity : AppCompatActivity() {
    lateinit var db: FirebaseFirestore
    lateinit var storage:FirebaseStorage
    lateinit var binding: ActivityNewsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        binding = ActivityNewsBinding.inflate(layoutInflater)
        val verMaisButton = binding.verMaisPupularNewsButton
        val searchButon = binding.searchNewsButton
        verMaisButton.setOnClickListener {
            println("Botão Ver Mais Clickado")
            println("Executar Ação")
        }

        searchButon.setOnClickListener {
            println("Botão Search Clickado")
            println("Executar Ação")
        }
        setContentView(binding.root)
        loadAllData()
    }

    private fun loadAllData() {
        val newsList = ArrayList<News>()
        val ref = db.collection("news")
        var items = 0
        var completeTasks = 0
        ref.get().addOnSuccessListener {
            if (it.isEmpty){
                Toast.makeText(this@NewsActivity, "Ops, parece que não foi possivel carregar as notcias"
                    , Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }
            for (news in it) {
                items += 1
                println("NEWS ID")
                println(news.id)
                val newsModel = news.toObject(News::class.java)
                val imageRef = storage.reference.child(newsModel.coverPath).downloadUrl
                imageRef.addOnSuccessListener { uri ->
                    newsModel.coverPath = uri.toString()
                    newsList.add(newsModel)
                    completeTasks += 1
                    whenListComplete(newsList, items, completeTasks)
                }
            }
        }
    }

    private fun whenListComplete(newsList: ArrayList<News>, items: Int, completeTasks: Int){
        if(items == completeTasks){
            binding.newsHorizontalList.apply {
                layoutManager = LinearLayoutManager(this@NewsActivity, RecyclerView.HORIZONTAL, false)
                adapter = HorizontalNewsAdapter(newsList, this@NewsActivity)
            }
            binding.newsVerticalList.apply {
                layoutManager = LinearLayoutManager(this@NewsActivity, RecyclerView.VERTICAL, false)
                adapter = VerticalNewsAdapter(newsList, this@NewsActivity)
            }
        }
        else{
            println("ITEMS:$items")
            println("ITEMS:$completeTasks")
        }
    }
}
