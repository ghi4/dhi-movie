package com.dhimas.dhiflix.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowEntity(
    var title: String? = null,
    var releaseYear: String? = null,
    var overview: String? = null,
    var posterPath: Int? = null,
    var backdropPath: Int? = null
) : Parcelable