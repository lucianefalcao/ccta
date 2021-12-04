package com.luciane.ccta.activity.news
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luciane.ccta.databinding.HorizontalNewsCardLayoutBinding
import com.luciane.ccta.model.News

class HorizontalNewsAdapter (val newsList : ArrayList<News>, val context: Context) :
    RecyclerView.Adapter<HorizontalNewsAdapter.MyNews>() {
    class MyNews(val binding: HorizontalNewsCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNews {
        val binding = HorizontalNewsCardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        val buttonShare = binding.favoriteButton
        val cardNews = binding.horizontalCardLayout

        cardNews.setOnClickListener {
            println("Botão News Clickado")
            println("Executar Ação")
        }
        buttonShare.setOnClickListener {
            println("Botão Favorito Clickado")
            println("Executar Ação")
        }
        return MyNews(binding)
    }

    override fun onBindViewHolder(holder: MyNews, position: Int){

        val news = newsList[position]
        with(holder) {
            binding.horizontalCardTitle.text = news.title
            val url = news.coverPath

            println(url)
            Glide.with(context)
                .load(news.coverPath)
                .into(binding.horizontalCardImage)
            binding.horizontalCardImage.clipToOutline = true
            binding.horizontalCardLayout.setOnClickListener {
//                newsDetail(news.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

//    private fun newsDetail(noticiaId: String){
//        //Log.d(NoticiasActivity.TAG, "Notícia: $noticiaId")
//        val intent = Intent(context, NewsDetailsActivity::class.java)
//        intent.putExtra("noticiaId", noticiaId)
//        context.startActivity(intent)
//    }
}
