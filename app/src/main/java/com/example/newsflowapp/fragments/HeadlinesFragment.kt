package com.example.newsflowapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsflowapp.ArticleDetailActivity
import com.example.newsflowapp.R
import com.example.newsflowapp.adapter.ArticleAdapter
import com.example.newsflowapp.data.NewsRepository
import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.model.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HeadlinesFragment : Fragment() {

  private lateinit var rvHeadlines: RecyclerView
  private lateinit var adapter: ArticleAdapter
  private val articlesList = getLatestArticles()
  private val scope = CoroutineScope(Job() + Dispatchers.Main)

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    loadHeadlines()
    val view = inflater.inflate(R.layout.fragment_headlines, container, false)
    rvHeadlines = view.findViewById(R.id.rvHeadlines)
    rvHeadlines.layoutManager = LinearLayoutManager(activity)
    adapter = ArticleAdapter(listOf()) { article -> showArticleDetails(article) }
    rvHeadlines.adapter = adapter
    adapter.updateArticles(articlesList)
    return view
  }

  private fun showArticleDetails(article: Article) {
    val intent = Intent(activity, ArticleDetailActivity::class.java).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL, article.url)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_TITLE, article.title)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_DESCRIPTION, article.description)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_CONTENT, article.content)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_SOURCE, article.sourceName)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_DATE, article.publishedAt)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_IMAGE, article.urlToImage)
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_CATEGORY, article.category)
    }
    startActivity(intent)
  }

  private fun loadHeadlines() {
    scope.launch {
      val result = NewsRepository.getTopHeadlines("technology")
      when (result) {
        is NewsRepository.Result.Success -> {
          adapter.updateArticles(result.data)
        }
        is NewsRepository.Result.Error -> {
          // Ako API poziv nije uspio, koristimo statičke podatke
          adapter.updateArticles(getLatestArticles())
          Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
        }
      }
    }
  }


  companion object {
    fun newInstance() = HeadlinesFragment()
  }
}