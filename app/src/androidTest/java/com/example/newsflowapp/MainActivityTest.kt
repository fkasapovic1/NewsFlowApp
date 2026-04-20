package com.example.newsflowapp

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import android.view.View
import androidx.test.espresso.intent.Intents.intended
import com.example.newsflowapp.data.getLatestArticles
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class MainActivityTest {


  @get:Rule
  val activityRule = ActivityScenarioRule(MainActivity::class.java)


  @Test
  fun testArticleListIsDisplayed() {
    onView(withId(R.id.rvArticles))
      .check(matches(isDisplayed()))
  }


  @Test
  fun testArticleListHasItems() {
    onView(withId(R.id.rvArticles))
      .check(hasItemCount(getLatestArticles().size))
  }


  @Test
  fun testClickOnArticleOpensDetail() {
    Intents.init()
    onView(withId(R.id.rvArticles)).perform(
      RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
        0, click()
      )
    )
    intended(hasComponent(ArticleDetailActivity::class.java.name))
    Intents.release()
  }


  @Test
  fun testSearchFieldAcceptsText() {
    onView(withId(R.id.etSearch))
      .perform(click(), typeText("android"))
    onView(withId(R.id.etSearch))
      .check(matches(withText("android")))
  }


  // Custom ViewAssertion za provjeru broja elemenata u RecyclerView
  private fun hasItemCount(n: Int) = object : ViewAssertion {
    override fun check(view: View?, ex: NoMatchingViewException?) {
      if (ex != null) throw ex
      assertTrue("View nije RecyclerView", view is RecyclerView)
      val rv = view as RecyclerView
      assertThat(
        "Broj elemenata u listi",
        rv.adapter?.itemCount,
        CoreMatchers.equalTo(n)
      )
    }
  }
}
