package com.dhimas.dhiflix.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateParseToMonthAndYear(date: String?): String {
        return if(!date.isNullOrEmpty()){
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            val newDate = parser.parse(date)

            if(newDate != null) {
                formatter.format(newDate)
            } else {
                date
            }
        } else {
            "No Date"
        }
    }

}