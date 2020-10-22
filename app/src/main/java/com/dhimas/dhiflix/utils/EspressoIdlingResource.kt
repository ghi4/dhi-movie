package com.dhimas.dhiflix.utils

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {
    private const val RESOURCE = "GLOBAL"
    val testIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        testIdlingResource.increment()
    }

    fun decrement() {
        testIdlingResource.decrement()
    }
}