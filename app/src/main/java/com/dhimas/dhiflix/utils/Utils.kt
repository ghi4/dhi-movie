package com.dhimas.dhiflix.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object Utils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, message: String, actionMessage: String, runnable: Runnable) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(actionMessage) {
                runnable.run()
            }
            .apply {
                anchorView = view
            }
            .show()
    }

}