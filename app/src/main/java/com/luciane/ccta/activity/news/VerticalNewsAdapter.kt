package com.luciane.ccta.activity.news
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luciane.ccta.databinding.VerticalNewsCardLayoutBinding
import com.luciane.ccta.model.News
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class VerticalNewsAdapter (val newsList : ArrayList<News>, val context: Context) :
    RecyclerView.Adapter<VerticalNewsAdapter.MyNews>() {

    class MyNews(val binding: VerticalNewsCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyNews {
        val binding = VerticalNewsCardLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        val buttonShare = binding.shareButton

        buttonShare.setOnClickListener {
            println("Botão Clickado")
            println("Executar Ação")
        }
        return MyNews(binding)
    }


    override fun onBindViewHolder(holder: MyNews, position: Int){

        val news = newsList[position]
        val formattedData = formatData(news.lastModified)
        with(holder) {
            binding.titleVerticalNews.text = news.title
            Glide.with(context)
                .load(news.coverPath)
                .into(binding.imageVerticalNews)
            binding.imageVerticalNews.clipToOutline = true
            binding.dateVerticalNews.text = formattedData
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    private fun formatData(lastModified: Long): String? {
        val dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(lastModified),
            ZoneId.systemDefault()
        )
        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
        return dateTime.format(formatter)
    }
}
