package com.example.newsflowapp

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSubstring
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticleDetailTest {


  @get:Rule
  val activityRule = ActivityScenarioRule<ArticleDetailActivity>(
    Intent(
      ApplicationProvider.getApplicationContext(),
      ArticleDetailActivity::class.java).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL,
               "https://example.com/android-15")
    }
  )


  @Test
  fun testTitleIsDisplayedCorrectly() {
    onView(withId(R.id.tvDetailTitle))
      .check(matches(withText("Android 15 donosi revolucionarne promjene")))
  }


  @Test
  fun testSourceIsDisplayed() {
    onView(withId(R.id.tvDetailSource))
      .check(matches(withText("TechNews")))
  }


  @Test
  fun testContentIsDisplayed() {
    onView(withId(R.id.tvDetailContent))
      .check(matches(isDisplayed()))
  }


  @Test
  fun testUrlIsDisplayed() {
    onView(withId(R.id.tvDetailUrl))
      .check(matches(withSubstring("example.com")))
  }

  @Test
  fun testClickOnUrlOpensIntent() {
    Intents.init()
    val intent = Intent(
      ApplicationProvider.getApplicationContext(),
      ArticleDetailActivity::class.java
    ).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL,
               "https://example.com/android-15")
    }
    launchActivity<ArticleDetailActivity>(intent)
    onView(withId(R.id.tvDetailUrl)).perform(click())
    intended(hasAction(Intent.ACTION_VIEW))
    Intents.release()
  }

  @Test
  fun testShareButtonLaunchesSendIntent() {
    Intents.init()
    val intent = Intent(
      ApplicationProvider.getApplicationContext(),
      ArticleDetailActivity::class.java
    ).apply {
      putExtra(ArticleDetailActivity.EXTRA_ARTICLE_URL,
               "https://example.com/android-15")
    }
    launchActivity<ArticleDetailActivity>(intent)
    onView(withId(R.id.fabShare)).perform(click())
    intended(hasAction(Intent.ACTION_CHOOSER))
    Intents.release()
  }


}
