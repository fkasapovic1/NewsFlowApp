package com.example.newsflowapp.data

import com.example.newsflowapp.model.Article

// data/NewsStaticData.kt
fun getLatestArticles(): List<Article> {
  return listOf(
    Article(
      id = 1,
      title = "Android 15 donosi revolucionarne promjene",
      description = "Google je najavio Android 15 s nizom novih funkcija...",
      content = "Puni sadržaj članka o Android 15...",
      url = "https://example.com/android-15",
      urlToImage = null,
      publishedAt = "2024-05-20",
      sourceName = "TechNews",
      category = "technology"
    ),
    Article(
      id = 2,
      title = "Novi rekord na berzama u 2024. godini",
      description = "Globalna tržišta dostigla su historijske vrhove...",
      content = "Puni sadržaj ekonomskog članka...",
      url = "https://example.com/markets-record",
      urlToImage = null,
      publishedAt = "2024-05-19",
      sourceName = "EconomyDaily",
      category = "business"
    ),
    Article(
      id = 3,
      title = "Reprezentacija BiH pred važnim susretima",
      description = "Selektor najavio sastav za naredne utakmice...",
      content = "Puni sadržaj sportskog članka...",
      url = "https://example.com/bih-football",
      urlToImage = null,
      publishedAt = "2024-05-18",
      sourceName = "SportInfo",
      category = "sports"
    ),
    // Dodajte još vijesti po želji
  )
}


fun getSavedArticles(): List<Article> {
  return listOf(
    Article(
      id = 10,
      title = "Kotlin Coroutines: kompletan vodič za 2024.",
      description = "Naučite sve o asinhronom programiranju u Kotlinu...",
      content = "Detaljan vodič o Kotlin Coroutines...",
      url = "https://example.com/kotlin-coroutines",
      urlToImage = null,
      publishedAt = "2024-05-15",
      sourceName = "DevBlog",
      category = "technology"
    ),
    // Dodajte još sačuvanih vijesti
  )
}
