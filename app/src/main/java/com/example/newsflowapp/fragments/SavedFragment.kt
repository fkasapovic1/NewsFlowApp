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
import com.example.newsflowapp.data.getSavedArticles
import com.example.newsflowapp.model.Article

class SavedFragment : Fragment() {

  private lateinit var rvSaved: RecyclerView
  private lateinit var adapter: ArticleAdapter
  private val savedList = getSavedArticles()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.fragment_saved, container, false)
    rvSaved = view.findViewById(R.id.rvSaved)
    rvSaved.layoutManager = LinearLayoutManager(activity)
    adapter = ArticleAdapter(listOf()) { article -> showArticleDetails(article) }
    rvSaved.adapter = adapter
    adapter.updateArticles(savedList)
    return view
  }

  private fun showArticleDetails(article: Article) {
    val intent = Intent(activity, ArticleDetailActivity::class.java).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL, article.url)
    }
    startActivity(intent)
  }

  companion object {
    fun newInstance() = SavedFragment()
  }
}