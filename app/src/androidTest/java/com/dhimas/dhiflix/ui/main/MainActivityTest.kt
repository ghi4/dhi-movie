package com.dhimas.dhiflix.ui.main

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityTest {
    private val dummyMovie = DummyData.generateDummyMovies()
    private val dummySeries = DummyData.generateDummySeries()

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

    @Test
    fun loadMovies() {
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummyMovie.size - 1
            )
        )
    }

    @Test
    fun loadDetailMovie() {
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                9,
                click()
            )
        )

        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_title)).check(matches(withText(dummyMovie[9].title)))
        onView(withId(R.id.tv_detail_release_year)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_release_year)).check(matches(withText(dummyMovie[9].releaseYear)))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_overview)).check(matches(withText(dummyMovie[9].overview)))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                9
            )
        )

        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                5,
                click()
            )
        )

        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_title)).check(matches(withText(dummyMovie[5].title)))
        onView(withId(R.id.tv_detail_release_year)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_release_year)).check(matches(withText(dummyMovie[5].releaseYear)))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_overview)).check(matches(withText(dummyMovie[5].overview)))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummyMovie.size - 2
            )
        )
    }

    @Test
    fun loadSeries() {
        onView(withId(R.id.navigation_series)).perform(click())
        onView(withId(R.id.rv_series)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_series)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummySeries.size - 1
            )
        )
    }

    @Test
    fun loadDetailSeries() {
        onView(withId(R.id.navigation_series)).perform(click())
        onView(withId(R.id.rv_series)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                9,
                click()
            )
        )

        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_title)).check(matches(withText(dummySeries[9].title)))
        onView(withId(R.id.tv_detail_release_year)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_release_year)).check(matches(withText(dummySeries[9].releaseYear)))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_overview)).check(matches(withText(dummySeries[9].overview)))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                9
            )
        )

        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                5,
                click()
            )
        )

        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_title)).check(matches(withText(dummySeries[5].title)))
        onView(withId(R.id.tv_detail_release_year)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_release_year)).check(matches(withText(dummySeries[5].releaseYear)))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
//        onView(withId(R.id.tv_detail_overview)).check(matches(withText(dummySeries[5].overview)))
        onView(withId(R.id.rv_other_movie)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                dummySeries.size - 2
            )
        )
    }

}