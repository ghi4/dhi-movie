package com.dhimas.dhiflix.utils

import android.content.Context
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

object Utils {

    fun loadingDrawable(ctx: Context): CircularProgressDrawable {
        val loadingCircular = CircularProgressDrawable(ctx)

        loadingCircular.strokeWidth = 4f
        loadingCircular.centerRadius = 30f
        loadingCircular.start()

        return loadingCircular
    }

    fun shimmeringDrawable(): ShimmerDrawable {
        val shimmer = Shimmer.AlphaHighlightBuilder()
            .setDuration(1000)
            .setBaseAlpha(0.7f)
            .setHighlightAlpha(0.6f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .setFixedWidth(200)
            .setFixedHeight(300)
            .build()

        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }



}