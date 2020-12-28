package com.dhimas.dhiflix.core.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateParseToMonthAndYear(stringDate: String?): String {
        return if (!stringDate.isNullOrEmpty()) {
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
                val newDate = parser.parse(stringDate)

                if (newDate != null) {
                    formatter.format(newDate)
                } else {
                    stringDate
                }
            } catch (e: Exception) {
                "No Date"
            }
        } else {
            "No Date"
        }
    }

}