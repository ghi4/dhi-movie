package com.dhimas.dhiflix.utils

class Const {
    companion object {
        //For load image
        const val URL_BASE_IMAGE = "https://image.tmdb.org/t/p/w500"

        //For poster image
        const val POSTER_TARGET_WIDTH = 200
        const val POSTER_TARGET_HEIGHT = 300

        //For backdrop image
        const val BACKDROP_TARGET_WIDTH = 1280
        const val BACKDROP_TARGET_HEIGHT = 720

        //If loading too fast. Shimmer look awkward.
        const val SHIMMER_TIME = 0L
        const val MINIMUM_SHIMMER_TIME = 0L

        //For knowing the show type (Movie/Series)
        const val MOVIE_TYPE = 0
        const val SERIES_TYPE = 1
    }
}