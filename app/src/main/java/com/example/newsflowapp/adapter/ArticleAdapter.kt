package com.example.newsflowapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsflowapp.R
import com.example.newsflowapp.model.Article

// adapter/ArticleAdapter.kt
class ArticleAdapter(
  private var articles: List<Article>,
  private val onItemClicked: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {


  inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ivImage: ImageView = view.findViewById(R.id.ivArticleImage)
    val tvTitle: TextView = view.findViewById(R.id.tvArticleTitle)
    val tvSource: TextView = view.findViewById(R.id.tvSource)
    val tvDate: TextView = view.findViewById(R.id.tvDate)
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_article, parent, false)
    return ArticleViewHolder(view)
  }


  override fun getItemCount() = articles.size


  override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
    val article = articles[position]
    holder.tvTitle.text = article.title
    holder.tvSource.text = article.sourceName
    holder.tvDate.text = article.publishedAt
    // Postavljamo ikonu kategorije iz drawable resursa
    val context = holder.ivImage.context
    val resId = context.resources.getIdentifier(
      article.category, "drawable", context.packageName
    )
    if (resId != 0) holder.ivImage.setImageResource(resId)
    else holder.ivImage.setImageResource(R.drawable.ic_news_placeholder)
    // Klik na element
    holder.itemView.setOnClickListener { onItemClicked(article) }
  }


  fun updateArticles(newArticles: List<Article>) {
    articles = newArticles
    notifyDataSetChanged()
  }
}
