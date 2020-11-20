package com.dhimas.dhiflix.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "showtable")
data class ShowEntity(

    @PrimaryKey
    @NonNull
    var id: String,

    var title: String? = null,

    var releaseDate: String? = null,

    var overview: String? = null,

    var posterPath: String? = null,

    var backdropPath: String? = null,

    var show_type: Int? = 0,

    var page: Int? = 0,

    var isFavorite: Int? = 0

) : Parcelable