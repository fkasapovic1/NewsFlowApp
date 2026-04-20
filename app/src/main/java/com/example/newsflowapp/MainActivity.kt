package com.example.newsflowapp

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsflowapp.adapter.ArticleAdapter
import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.model.Article
import com.example.newsflowapp.receiver.NetworkChangeReceiver

class MainActivity : AppCompatActivity() {
  private lateinit var rvArticles: RecyclerView
  private lateinit var etSearch: EditText
  private lateinit var adapter: ArticleAdapter
  private var articlesList = getLatestArticles()
  private lateinit var networkReceiver: NetworkChangeReceiver


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    rvArticles = findViewById(R.id.rvArticles)
    rvArticles.layoutManager = LinearLayoutManager(this)
    adapter = ArticleAdapter(listOf()) { article ->
      showArticleDetails(article)
    }
    rvArticles.adapter = adapter
    adapter.updateArticles(articlesList)

    if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
      handleSharedText(intent)
    }

  }


  // U MainActivity.kt
  private fun showArticleDetails(article: Article) {
    val intent = Intent(this, ArticleDetailActivity::class.java).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL, article.url)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_TITLE, article.title)
    }
    startActivity(intent)
  }

  private fun handleSharedText(intent: Intent) {
    intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
      // Postavljamo primljeni tekst u search polje
      etSearch.setText(sharedText)
      // Automatski pokrenemo pretragu
      performSearch(sharedText)
    }
  }

  private fun performSearch(query: String) {
    val results = articlesList.filter {
      it.title.contains(query, ignoreCase = true) ||
          it.description?.contains(query, ignoreCase = true) == true
    }
    adapter.updateArticles(results)
  }

  override fun onResume() {
    super.onResume()
    networkReceiver = NetworkChangeReceiver()
    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
    registerReceiver(networkReceiver, filter)
  }


  override fun onPause() {
    super.onPause()
    unregisterReceiver(networkReceiver)
  }



}
