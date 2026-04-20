package com.example.newsflowapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast

// receiver/NetworkChangeReceiver.kt
class NetworkChangeReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    val connectivityManager = context.getSystemService(
      Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val capabilities = connectivityManager.getNetworkCapabilities(network)
    val isConnected = capabilities?.hasCapability(
      NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    if (!isConnected) {
      Toast.makeText(
        context,
        "Internet veza je prekinuta. Prikazuju se keširani podaci.",
        Toast.LENGTH_LONG
      ).show()
    } else {
      Toast.makeText(
        context,
        "Internet veza je uspostavljena.",
        Toast.LENGTH_SHORT
      ).show()
    }
  }
}
