package com.dhimas.dhiflix.ui.main

import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.R
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loadMovies() {
        //Check if banner is displayed
        //Check if recyclerView is displayed and can scroll
        onView(withId(R.id.vp_movie_banner)).check(matches(isDisplayed()))
        onView(withId(R.id.vp_movie_banner)).perform(swipeLeft())
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.nestedScroll_movie)).perform(swipeUp())
    }

    @Test
    fun loadSeries() {
        //Go to series fragment
        onView(withId(R.id.navigation_series)).perform(click())

        //Check if banner is displayed
        //Check if recyclerView is displayed and can scroll
        onView(withId(R.id.vp_series_banner)).check(matches(isDisplayed()))
        onView(withId(R.id.vp_series_banner)).perform(swipeLeft())
        onView(withId(R.id.rv_series)).check(matches(isDisplayed()))
        onView(withId(R.id.nestedScroll_series)).perform(swipeUp())
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

        //Click on item 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                7,
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

        //Click on item 7
        onView(withId(R.id.rv_detail_other_shows)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                7,
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

        //Type "Planet Mars dancing with cat the moon" to searchView and pressAction
        val impossibleTitle = "Planet Mars dancing with cat in the moon"
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
        onView(isRoot()).perform(swipeLeft()) //To series tab

        //IN SERIES TAB
        onView(withId(R.id.iv_search_series_info)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_search_series_info)).check(matches(isDisplayed()))
        onView(isRoot()).perform(swipeLeft()) //To movie tab
    }

    /**
     * MUST HAVE NO FAVORITE LIST TO RUN THIS
     * */

    @Test
    fun emptyFavorite() {
        //Go to favorite fragment
        onView(withId(R.id.navigation_favorite)).perform(click())

        //When there is no favorite movies/series
        //Check if imageView and textView info is displayed
        //Check if recyclerView is not displayed
        onView(withId(R.id.tv_favorite_info)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_favorite_info)).check(matches(isDisplayed()))
        //Check if textView title and recyclerView is displayed
        onView(withId(R.id.tv_favorite_movie_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_favorite_movie)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_favorite_series_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_favorite_series)).check(matches(not(isDisplayed())))
    }

    @Test
    fun addAndDeleteFavorite() {
        //Add data to favorite//

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
        onView(withId(R.id.tv_favorite_movie_title)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_favorite_series_title)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_favorite_series)).check(matches(isDisplayed()))

        //Delete data from favorite//

        onView(withId(R.id.rv_favorite_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.bt_detail_favorite)).perform(click())
        onView(isRoot()).perform(pressBack())
        onView(withId(R.id.rv_favorite_series)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                click()
            )
        )
        onView(withId(R.id.bt_detail_favorite)).perform(click())
        onView(isRoot()).perform(pressBack())

        //Check if imageView and textView info is displayed
        //Check if recyclerView is not displayed
        onView(withId(R.id.tv_favorite_info)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_favorite_info)).check(matches(isDisplayed()))
        //Check if textView title and recyclerView is displayed
        onView(withId(R.id.tv_favorite_movie_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_favorite_movie)).check(matches(not(isDisplayed())))
        onView(withId(R.id.tv_favorite_series_title)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_favorite_series)).check(matches(not(isDisplayed())))
    }

}