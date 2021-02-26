package com.dhimas.dhiflix.core.utils

import org.junit.Assert.*
import org.junit.Test

class UtilsTest {

    @Test
    fun dateParser() {
        val dateString = "2020-02-24"
        val expectedResult = "February 2020"
        val result = Utils.dateParseToMonthAndYear(dateString)

        assertEquals(expectedResult, result)
    }

    @Test
    fun dateParserNull() {
        val dateString = ""
        val expectedResult = "No Date"
        val result = Utils.dateParseToMonthAndYear(dateString)

        assertEquals(expectedResult, result)
    }
}