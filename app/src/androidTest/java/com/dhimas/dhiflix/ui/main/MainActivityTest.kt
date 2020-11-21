package com.dhimas.dhiflix.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.testIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.testIdlingResource)
    }

//    @Test
//    fun loadMovies() {
//        waitHandler()
//        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
//        onView(withId(R.id.rv_movie)).perform(
//            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
//                7
//            )
//        )
//    }
//
//    @Test
//    fun loadSeries() {
//        onView(withId(R.id.navigation_series)).perform(click())
//
//        onView(withId(R.id.rv_series)).check(matches(isDisplayed()))
//        onView(withId(R.id.rv_series)).perform(
//            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
//                7
//            )
//        )
//    }
//
//    @Test
//    fun loadFavorite() {
//        onView(withId(R.id.navigation_favorite)).perform(click())
//
//        onView(withId(R.id.rv_favorite_movie)).check(matches(not(isDisplayed())))
//    }

    @Test
    fun loadDetailMovie() {
        waitHandler()

        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        waitHandler()
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        )

        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        waitHandler()
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                1
            )
        )
    }

    @Test
    fun loadDetailSeries() {
        waitHandler()
        onView(withId(R.id.navigation_series)).perform(click())

        waitHandler()
        onView(withId(R.id.rv_series)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        waitHandler()
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )

        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                7,
                click()
            )
        )

        waitHandler()
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )
    }

    private fun waitHandler() {
        Thread.sleep(3000L)
    }
}