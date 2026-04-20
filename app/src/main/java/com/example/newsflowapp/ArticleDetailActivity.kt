package com.example.newsflowapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.data.getSavedArticles
import com.example.newsflowapp.model.Article
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArticleDetailActivity : AppCompatActivity() {


  private lateinit var article: Article
  private lateinit var tvTitle: TextView
  private lateinit var tvSource: TextView
  private lateinit var tvDate: TextView
  private lateinit var tvUrl: TextView
  private lateinit var tvContent: TextView
  private lateinit var ivHeader: ImageView
  private lateinit var fabShare: FloatingActionButton


  companion object {
    const val EXTRA_ARTICLE_URL = "article_url"
    const val EXTRA_ARTICLE_TITLE = "article_title"
  }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_article_detail)
    initViews()
    val url = intent.getStringExtra(EXTRA_ARTICLE_URL) ?: run {
      finish()
      return
    }
    article = getArticleByUrl(url) ?: run {
      finish()
      return
    }
    populateDetails()
  }


  private fun initViews() {
    tvTitle = findViewById(R.id.tvDetailTitle)
    tvSource = findViewById(R.id.tvDetailSource)
    tvDate = findViewById(R.id.tvDetailDate)
    tvUrl = findViewById(R.id.tvDetailUrl)
    tvContent = findViewById(R.id.tvDetailContent)
    ivHeader = findViewById(R.id.ivArticleHeader)
    fabShare = findViewById(R.id.fabShare)
  }


  private fun populateDetails() {
    tvTitle.text = article.title
    tvSource.text = article.sourceName
    tvDate.text = article.publishedAt
    tvUrl.text = article.url
    tvContent.text = article.content ?: article.description
    val resId = resources.getIdentifier(
      article.category, "drawable", packageName
    )
    if (resId != 0) ivHeader.setImageResource(resId)
    tvUrl.setOnClickListener { openArticleInBrowser() }
    fabShare.setOnClickListener { shareArticle() }
  }


  private fun getArticleByUrl(url: String): Article? {
    val allArticles = getLatestArticles() + getSavedArticles()
    return allArticles.find { it.url == url }
  }

  private fun openArticleInBrowser() {
    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
    try {
      startActivity(webIntent)
    } catch (e: ActivityNotFoundException) {
      Toast.makeText(this, "Nema instaliranog pregledača", Toast.LENGTH_SHORT).show()
    }
  }

  private fun shareArticle() {
    val shareText = "${article.title}\n\n${article.url}"
    val shareIntent = Intent().apply {
      action = Intent.ACTION_SEND
      type = "text/plain"
      putExtra(Intent.EXTRA_TEXT, shareText)
      putExtra(Intent.EXTRA_SUBJECT, article.title)
    }
    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_article)))
  }


}
