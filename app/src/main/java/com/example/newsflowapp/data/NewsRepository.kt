package com.example.newsflowapp.data

import android.net.Uri
import android.util.Log
import com.example.newsflowapp.model.Article
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

object NewsRepository {


  private const val BASE_URL = "https://newsapi.org/v2/"
  private const val API_KEY = "befd3c5feb2a45deb7ea1635b8fa4a1d"


  sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
  }


  suspend fun searchArticles(query: String): Result<List<Article>> {
    return withContext(Dispatchers.IO) {
      try {
        val encodedQuery = Uri.encode(query)
        val url = URL("${BASE_URL}everything?q=$encodedQuery" +
                          "&language=en&sortBy=publishedAt&pageSize=20&apiKey=$API_KEY")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty(
          "User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        )
        connection.connectTimeout = 10_000
        connection.readTimeout = 10_000
        val responseCode = connection.responseCode
        if (responseCode != HttpURLConnection.HTTP_OK) {
          return@withContext Result.Error("HTTP greška: $responseCode")
        }
        val json = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
        val articles = parseArticles(json)
        Result.Success(articles)
      } catch (e: MalformedURLException) {
        Result.Error("Neispravna URL adresa")
      } catch (e: IOException) {
        Result.Error("Mrežna greška — provjeri internet vezu")
      } catch (e: JSONException) {
        Result.Error("Greška parsiranja odgovora")
      }
    }
  }


  suspend fun getTopHeadlines(category: String = "technology"): Result<List<Article>> {
    return withContext(Dispatchers.IO) {
      try {
        val url = URL("${BASE_URL}top-headlines?country=us" +
                          "&category=$category&pageSize=20&apiKey=$API_KEY")
        val connection = url.openConnection() as HttpURLConnection
        connection.setRequestProperty(
          "User-Agent",
          "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
        )


        val json = connection.inputStream.bufferedReader().readText()
        connection.disconnect()
        Result.Success(parseArticles(json))
      } catch (e: IOException) {
        Result.Error("Mrežna greška")
      } catch (e: JSONException) {
        Result.Error("Greška parsiranja")
      }
    }
  }


  private fun parseArticles(json: String): List<Article> {
    val articles = mutableListOf<Article>()
    val jsonObject = JSONObject(json)
    val results = jsonObject.getJSONArray("articles")
    for (i in 0 until results.length()) {
      val item = results.getJSONObject(i)
      val source = item.getJSONObject("source")
      articles.add(Article(
        id = i.toLong(),
        title = item.optString("title", ""),
        description = item.optString("description").ifEmpty { null },
        content = item.optString("content").ifEmpty { null },
        url = item.optString("url", ""),
        urlToImage = item.optString("urlToImage").ifEmpty { null },
        publishedAt = item.optString("publishedAt", ""),
        sourceName = source.optString("name", "Nepoznat izvor"),
        category = null
      ))
    }
    return articles
  }
}
