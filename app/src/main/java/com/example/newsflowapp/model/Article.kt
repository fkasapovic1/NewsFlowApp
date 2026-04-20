package com.example.newsflowapp.model

data class Article(
  val id: Long,
  val title: String,
  val description: String?,
  val content: String?,
  val url: String,
  val urlToImage: String?,
  val publishedAt: String,
  val sourceName: String,
  val category: String
)
