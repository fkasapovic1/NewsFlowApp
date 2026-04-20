package com.example.newsflowapp

import com.example.newsflowapp.data.getLatestArticles
import com.example.newsflowapp.model.Article
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.everyItem
import org.hamcrest.Matchers.hasItem
import org.hamcrest.Matchers.hasProperty
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.isEmptyString
import org.junit.Assert.assertTrue
import org.junit.Test

class ArticleUnitTest {


  @Test
  fun testGetLatestArticlesNotEmpty() {
    val articles = getLatestArticles()
    assertTrue("Lista vijesti ne smije biti prazna", articles.isNotEmpty())
  }


  @Test
  fun testGetLatestArticlesCount() {
    val articles = getLatestArticles()
    // Trebamo imati barem 3 testne vijesti
    assertTrue("Treba biti barem 3 vijesti", articles.size >= 3)
  }


  @Test
  fun testArticlesHaveTitle() {
    val articles = getLatestArticles()
    assertThat(articles, everyItem(hasProperty("title", not(isEmptyString()))))
  }


  @Test
  fun testSpecificArticleExists() {
    val articles = getLatestArticles()
    assertThat(
      articles,
      hasItem<Article>(
        hasProperty("sourceName", `is`("TechNews"))
      )
    )
  }


  @Test
  fun testNoArticleWithEmptyUrl() {
    val articles = getLatestArticles()
    assertThat(
      articles,
      not(hasItem<Article>(hasProperty("url", isEmptyString())))
    )
  }


  @Test
  fun testArticleCategoryIsValid() {
    val validCategories = setOf(
      "technology", "sports", "business",
      "health", "entertainment", "science"
    )
    val articles = getLatestArticles()
    articles.forEach { article ->
      assertTrue(
        "Kategorija '${article.category}' nije validna",
        article.category in validCategories
      )
    }
  }
}
