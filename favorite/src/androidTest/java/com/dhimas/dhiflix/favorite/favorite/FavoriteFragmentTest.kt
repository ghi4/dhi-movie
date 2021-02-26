package com.dhimas.dhiflix.favorite.favorite

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.dhimas.dhiflix.favorite.R
import com.dhimas.dhiflix.ui.main.MainActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class FavoriteFragmentTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun loadFavorite() {
//        onView(withId(R.id.navigation_favorite)).perform(click())
//
//        onView(withId(R.id.iv_favorite_info)).check(matches(isDisplayed()))
    }
}