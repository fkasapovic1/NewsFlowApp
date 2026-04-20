package com.example.newsflowapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsflowapp.ArticleDetailActivity
import com.example.newsflowapp.R
import com.example.newsflowapp.adapter.ArticleAdapter
import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.model.Article

class HeadlinesFragment : Fragment() {

  private lateinit var rvHeadlines: RecyclerView
  private lateinit var adapter: ArticleAdapter
  private val articlesList = getLatestArticles()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
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
    }
    startActivity(intent)
  }

  companion object {
    fun newInstance() = HeadlinesFragment()
  }
}