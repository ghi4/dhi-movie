package com.dhimas.dhiflix.data.source.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//ce041f4e3f3987fb8580b0cf374393a6
@Parcelize
data class ShowEntity(
    var id: String? = null,

    var title: String? = null,

    var releaseDate: String? = null,

    var overview: String? = null,

    var posterPath: String? = null,

    var backdropPath: String? = null
) : Parcelable