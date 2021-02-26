package com.dhimas.dhiflix.ui.main

import android.widget.EditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.R
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    private fun waiter() {
        Thread.sleep(2000L)
    }

    @Test
    fun movieFragment() {
        waiter()
        onView(withId(R.id.vp_movie_banner)).check(matches(isDisplayed()))
        onView(withId(R.id.vp_movie_banner)).perform(swipeLeft())
        onView(withId(R.id.nestedScroll_movie)).perform(swipeUp())
    }

    @Test
    fun seriesFragment() {
        onView(withId(R.id.navigation_series)).perform(click())
        waiter()
        onView(withId(R.id.vp_series_banner)).check(matches(isDisplayed()))
        onView(withId(R.id.vp_series_banner)).perform(swipeLeft())
        onView(withId(R.id.nestedScroll_series)).perform(swipeUp())
    }

    @Test
    fun introSearchShows() {
        //Go to search fragment
        onView(withId(R.id.navigation_search)).perform(click())

        // 1
        // Check imageView and textView info is displayed

        //IN MOVIE TAB
        onView(withId(R.id.sv_search)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_search_movie_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_movie_info)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_series_info)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeRight()) //To movie tab
    }

    @Test
    fun searchSomeShows() {
        //Go to search fragment
        onView(withId(R.id.navigation_search)).perform(click())

        // 2
        // Search some movie and series
        // imageView info and textView info must not displayed
        // recyclerView must displayed

        //Type "Mulan" to searchView and pressAction
        val title = "Mulan"
        onView(isAssignableFrom(EditText::class.java)).perform(
            typeText(title),
            pressImeActionButton()
        )

        waiter()
        onView(isRoot()).perform(swipeLeft()) //To series tab
        onView(isRoot()).perform(swipeRight()) //To movie tab

        //IN MOVIE TAB
        onView(withId(R.id.iv_search_movie_info)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.tv_search_movie_info)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.rv_search_movie)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.tv_search_series_info)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.rv_search_series)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeRight()) //To movie tab
    }

    @Test
    fun searchImpossibleShows() {
        //Go to search fragment
        onView(withId(R.id.navigation_search)).perform(click())

        // 3
        // Search impossible movie and series
        // imageView info and textView must displayed

        //Type "Planet Mars dancing with cat the moon" to searchView and pressAction
        val impossibleTitle = "Planet Mars dancing with cat in the moon"
        onView(withId(R.id.sv_search)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(
            typeText(impossibleTitle),
            pressImeActionButton()
        )

        waiter()
        onView(isRoot()).perform(swipeLeft()) //To series tab
        onView(isRoot()).perform(swipeRight()) //To movie tab

        //IN MOVIE TAB
        onView(withId(R.id.iv_search_movie_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_movie_info)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_series_info)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To movie tab
    }
}