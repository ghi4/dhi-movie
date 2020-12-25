package com.dhimas.dhiflix.core.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun dateParseToMonthAndYear(date: String?): String {
        return if (!date.isNullOrEmpty()) {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
            val newDate = parser.parse(date)

            if (newDate != null) {
                formatter.format(newDate)
            } else {
                date
            }
        } else {
            "No Date"
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, message: String, runnable: Runnable) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setAction("RETRY") {
                runnable.run()
            }
            .apply {
                anchorView = view
            }
            .show()
    }

}