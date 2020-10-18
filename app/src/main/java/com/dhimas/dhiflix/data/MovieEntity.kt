package com.dhimas.dhiflix.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieEntity (
    var title: String? = null,
    var releaseYear: String? = null,
    var score: String? = null,
    var overview: String? = null,
    var posterPath: Int? = null,
    var backdropPath: Int? = null
) : Parcelable