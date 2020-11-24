package com.dhimas.dhiflix.ui.main

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import org.hamcrest.Matchers.not
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

    /**
     * MOVIE FRAGMENT
     *  - Check if recycler view is displayed
     *  - Check scrolling
     * */

    @Test
    fun loadMovies() {
        // Check if recyclerView is displayed and can scroll
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.nestedScroll_movie)).perform(swipeUp())
    }

    @Test
    fun loadSeries() {
        //Go to series fragment
        onView(withId(R.id.navigation_series)).perform(click())

        //Check if recyclerView is displayed and can scroll
        onView(withId(R.id.rv_series)).check(matches(isDisplayed()))
        onView(withId(R.id.nestedScroll_series)).perform(swipeUp())
    }

    //MUST HAVE NO FAVORITE TO RUN THIS//
    @Test
    fun loadFavorite() {
        //Go to favorite fragment
        onView(withId(R.id.navigation_favorite)).perform(click())

        //When there is no favorite movies/series
        //Check if imageView and textView info is displayed
        //Check if recyclerView is not displayed
        onView(withId(R.id.tv_favorite_info)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_favorite_info)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_movie)).check(matches(not(isDisplayed())))

        //Go to movie fragment
        onView(withId(R.id.navigation_movie)).perform(click())

        //Click the poster
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Click favorite button
        onView(withId(R.id.bt_detail_favorite)).perform(click())

        //Go back to movie fragment
        onView(isRoot()).perform(pressBack())

        //Go to series fragment
        onView(withId(R.id.navigation_series)).perform(click())

        //Click on item
        onView(withId(R.id.rv_series)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Click favorite button
        onView(withId(R.id.bt_detail_favorite)).perform(click())

        //Go back to series fragment
        onView(isRoot()).perform(pressBack())

        //Go to favorite fragment
        onView(withId(R.id.navigation_favorite)).perform(click())

        //When there is data in favorite
        //Check if imageView and textView info is not displayed
        onView(withId(R.id.iv_favorite_info)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_favorite_info)).check(matches(not(isDisplayed())))
        //Check if textView title and recyclerView is displayed
        onView(withId(R.id.tv_movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_favorite_series_title)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_series)).check(matches(isDisplayed()))
    }

    @Test
    fun searchShows() {
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

        onView(isRoot()).perform(swipeLeft()) //To series tab
        onView(isRoot()).perform(swipeRight()) //To movie tab

        //IN MOVIE TAB
        onView(withId(R.id.iv_search_movie_info)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_search_movie_info)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_search_movie)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_search_series_info)).check(matches(not(isDisplayed())))
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

        //Type "I get five star in the sky" to searchView and pressAction
        val impossibleTitle = "I get five stars in the sky"
        onView(withId(R.id.sv_search)).perform(click())
        onView(isAssignableFrom(EditText::class.java)).perform(
            typeText(impossibleTitle),
            pressImeActionButton()
        )

        onView(isRoot()).perform(swipeLeft()) //To series tab
        onView(isRoot()).perform(swipeRight()) //To movie tab

        //IN MOVIE TAB
        onView(withId(R.id.iv_search_movie_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_movie_info)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_search_movie)).check(matches(not(isDisplayed())))
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_series_info)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_search_series)).check(matches(not(isDisplayed())))
        onView(isRoot()).perform(swipeLeft()) //To movie tab
    }

    @Test
    fun loadDetailMovie() {
        //Click on item
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Check if all view is displayed
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_releaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        //Scroll down
        onView(isRoot()).perform(swipeUp())
        //Scroll recyclerView to position 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )

        //Click on item 1
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Check if all view is displayed
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_releaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        //Scroll down
        onView(isRoot()).perform(swipeUp())
        //Scroll recyclerView to position 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )
    }

    @Test
    fun loadDetailSeries() {
        //Go to series fragment
        onView(withId(R.id.navigation_series)).perform(click())

        //Click on item 1
        onView(withId(R.id.rv_series)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Check if all view is displayed
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_releaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        //Scroll down
        onView(isRoot()).perform(swipeUp())
        //Scroll recyclerView to position 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )

        //Click on item 1
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                click()
            )
        )

        //Check if all view is displayed
        onView(withId(R.id.iv_detail_backdrop)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_detail_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_title)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_releaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_detail_overview)).check(matches(isDisplayed()))
        //Scroll down
        onView(isRoot()).perform(swipeUp())
        //Scroll recyclerView to position 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                7
            )
        )
    }

}