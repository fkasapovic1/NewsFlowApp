package com.example.newsflowapp

import android.app.NotificationManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.newsflowapp.service.NewsHeadlineService

class ArticleDetailResultActivity : AppCompatActivity() {


  private lateinit var tvTitle: TextView
  private lateinit var tvContent: TextView
  private lateinit var tvUrl: TextView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_article_detail)
    tvTitle = findViewById(R.id.tvDetailTitle)
    tvContent = findViewById(R.id.tvDetailContent)
    tvUrl = findViewById(R.id.tvDetailUrl)


    // Gasimo notifikaciju čim se aktivnost otvori
    val notifManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    notifManager.cancel(NewsHeadlineService.LATEST_NOTIFICATION_ID)


    // Primamo podatke iz PendingIntent-a
    val title = intent?.getStringExtra("article_title") ?: return
    val url = intent?.getStringExtra("article_url") ?: return
    tvTitle.text = title
    tvUrl.text = url
    tvContent.text = "Otvorite originalnu web stranicu za puni sadržaj."


    tvUrl.setOnClickListener {
      try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
      } catch (e: ActivityNotFoundException) { }
    }
  }
}
