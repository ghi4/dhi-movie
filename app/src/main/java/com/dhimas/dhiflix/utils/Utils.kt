package com.dhimas.dhiflix.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.dhimas.dhiflix.R
import com.google.android.material.snackbar.Snackbar

object Utils {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(context: Context, view: View, message: String?, runnable: Runnable) {
        val safeMessage = message ?: context.getString(R.string.unknown_error)
        Snackbar.make(view, safeMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction(context.getText(R.string.retry)) {
                runnable.run()
            }
            .apply {
                anchorView = view
            }
            .show()
    }

}