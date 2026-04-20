package com.example.newsflowapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsflowapp.ArticleDetailActivity
import com.example.newsflowapp.R
import com.example.newsflowapp.adapter.ArticleAdapter
import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.model.Article

class SearchFragment : Fragment() {

  private lateinit var etSearch: EditText
  private lateinit var btnSearch: ImageButton
  private lateinit var rvResults: RecyclerView
  private lateinit var tvNoResults: TextView
  private lateinit var adapter: ArticleAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_search, container, false)
    etSearch = view.findViewById(R.id.etSearch)
    btnSearch = view.findViewById(R.id.btnSearch)
    rvResults = view.findViewById(R.id.rvSearchResults)
    tvNoResults = view.findViewById(R.id.tvNoResults)
    rvResults.layoutManager = LinearLayoutManager(activity)
    adapter = ArticleAdapter(listOf()) { article -> showArticleDetails(article) }
    rvResults.adapter = adapter

    // Inicijalni tekst pretrage proslijeđen iz MainActivity
    arguments?.getString("search")?.let { query ->
      if (query.isNotEmpty()) {
        etSearch.setText(query)
        performSearch(query)
      }
    }

    btnSearch.setOnClickListener {
      performSearch(etSearch.text.toString())
    }

    return view
  }

  private fun performSearch(query: String) {
    if (query.isBlank()) return
    val results = getLatestArticles().filter {
      it.title.contains(query, ignoreCase = true) ||
          it.description?.contains(query, ignoreCase = true) == true
    }
    adapter.updateArticles(results)
    tvNoResults.visibility = if (results.isEmpty()) View.VISIBLE else View.GONE
    rvResults.visibility = if (results.isEmpty()) View.GONE else View.VISIBLE
  }

  private fun showArticleDetails(article: Article) {
    val intent = Intent(activity, ArticleDetailActivity::class.java).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL, article.url)
    }
    startActivity(intent)
  }

  companion object {
    fun newInstance() = SearchFragment()
  }
}