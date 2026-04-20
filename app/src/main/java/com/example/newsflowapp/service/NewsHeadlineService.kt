package com.example.newsflowapp.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsflowapp.ArticleDetailActivity
import com.example.newsflowapp.ArticleDetailResultActivity
import com.example.newsflowapp.MainActivity
import com.example.newsflowapp.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NewsHeadlineService : Service() {


  private var wakeLock: PowerManager.WakeLock? = null
  private var isServiceStarted = false


  companion object {
    const val CHANNEL_ID = "news_headline_channel"
    const val NOTIFICATION_ID = 1
    const val LATEST_NOTIFICATION_ID = 2
  }


  override fun onBind(intent: Intent): IBinder? = null


  override fun onCreate() {
    super.onCreate()
    // Odmah prikazujemo inicijalnu notifikaciju
    val notification = createInitialNotification()
    startForeground(NOTIFICATION_ID, notification)
  }


  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    startNewsService()
    return START_STICKY
  }


  override fun onDestroy() {
    super.onDestroy()
    isServiceStarted = false
    wakeLock?.let {
      if (it.isHeld) it.release()
    }
  }

  private fun createInitialNotification(): Notification {
    val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager


    // Kreiranje kanala — potrebno jednom, Android ga pamti
    val channel = NotificationChannel(
      CHANNEL_ID,
      "NewsFlow vijesti",
      NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
      description = "Obavijesti o najnovijim vijestima"
      enableLights(true)
      lightColor = Color.BLUE
    }
    notificationManager.createNotificationChannel(channel)


    // Klik na notifikaciju otvara MainActivity
    val openAppIntent = Intent(this, MainActivity::class.java)
    val pendingIntent = PendingIntent.getActivity(
      this, 0, openAppIntent,
      PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )


    return NotificationCompat.Builder(this, CHANNEL_ID)
      .setContentTitle("NewsFlow")
      .setContentText("Pratimo najnovije vijesti za vas...")
      .setSmallIcon(android.R.drawable.ic_menu_info_details)
      .setContentIntent(pendingIntent)
      .setOngoing(true)
      .build()
  }

  private fun startNewsService() {
    if (isServiceStarted) return
    isServiceStarted = true


    // WakeLock za sprječavanje prekida u Doze modu
    wakeLock = (getSystemService(POWER_SERVICE) as PowerManager).run {
      newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NewsFlow::HeadlineService")
        .apply { acquire(10 * 60 * 1000L) } // Max 10 minuta odjednom
    }


    GlobalScope.launch(Dispatchers.IO) {
      while (isServiceStarted) {
        launch(Dispatchers.IO) {
          fetchLatestHeadline()
        }
        // Čekamo sat vremena
        delay(60 * 60 * 1000L)
      }
    }
  }

  private fun fetchLatestHeadline() {
    try {
      val url = URL("https://newsapi.org/v2/top-headlines" +
                        "?country=us&pageSize=1&apiKey=${BuildConfig.NEWS_API_KEY}")
      val connection = url.openConnection() as HttpURLConnection

      // POSTAVLJANJE USER-AGENTA (Obavezno za NewsAPI)
      connection.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
      )
      connection.connectTimeout = 15_000
      connection.readTimeout = 15_000

      // Provjeravamo da li je odgovor uspješan (200 OK)
      val responseCode = connection.responseCode
      if (responseCode == HttpURLConnection.HTTP_OK) {
        val json = connection.inputStream.bufferedReader().use { it.readText() }
        connection.disconnect()

        val jsonObject = JSONObject(json)
        val articles = jsonObject.getJSONArray("articles")
        if (articles.length() == 0) return

        val first = articles.getJSONObject(0)
        val title = first.optString("title", "Nova vijest dostupna")
        val articleUrl = first.optString("url", "")

        // Intent koji otvara ArticleDetailResultActivity
        // Koristimo this@NewsHeadlineService da budemo eksplicitni unutar coroutine
        val detailIntent = Intent(this@NewsHeadlineService, ArticleDetailResultActivity::class.java).apply {
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
          putExtra("article_url", articleUrl)
          putExtra("article_title", title)
        }

        val pendingIntent = PendingIntent.getActivity(
          this@NewsHeadlineService,
          0,
          detailIntent,
          PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Kreiranje nove notifikacije s naslovom vijesti
        val notification = NotificationCompat.Builder(this@NewsHeadlineService, CHANNEL_ID)
          .setContentTitle("Najnovija vijest")
          .setContentText(title)
          .setSmallIcon(android.R.drawable.ic_menu_info_details)
          .setContentIntent(pendingIntent)
          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
          .setAutoCancel(true)
          .build()

        // Slanje notifikacije
        val notifManager = NotificationManagerCompat.from(this@NewsHeadlineService)
        if (ActivityCompat.checkSelfPermission(
            this@NewsHeadlineService,
            Manifest.permission.POST_NOTIFICATIONS
          ) == PackageManager.PERMISSION_GRANTED
        ) {
          notifManager.notify(LATEST_NOTIFICATION_ID, notification)
        }
      } else {
        Log.e("NewsHeadlineService", "Server vratio grešku: $responseCode")
        connection.disconnect()
      }

    } catch (e: IOException) {
      Log.e("NewsHeadlineService", "Mrežna greška: ${e.message}")
    } catch (e: JSONException) {
      Log.e("NewsHeadlineService", "Greška parsiranja: ${e.message}")
    } catch (e: Exception) {
      Log.e("NewsHeadlineService", "Neočekivana greška: ${e.message}")
    }
  }


}
