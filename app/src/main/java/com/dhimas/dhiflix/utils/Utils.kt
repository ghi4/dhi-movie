package com.dhimas.dhiflix.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateParseToMonthAndYear(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)

        return formatter.format(parser.parse(date)!!)
    }

}