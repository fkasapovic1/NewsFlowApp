package com.example.newsflowapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.navHostFragment) as NavHostFragment
    val navController = navHostFragment.navController
    val bottomNavigation: BottomNavigationView = findViewById(R.id.bottomNavigation)
    bottomNavigation.setupWithNavController(navController)

    // Rukovanje dijeljenim tekstom iz Vježbe 3
    if (intent?.action == Intent.ACTION_SEND && intent.type == "text/plain") {
      intent.getStringExtra(Intent.EXTRA_TEXT)?.let { sharedText ->
        val bundle = bundleOf("search" to sharedText)
        bottomNavigation.selectedItemId = R.id.searchFragment
        navController.navigate(R.id.searchFragment, bundle)
      }
    }
  }
}