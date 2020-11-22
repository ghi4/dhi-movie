package com.dhimas.dhiflix.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

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

    var showType: Int? = 0,

    var page: Int? = 0,

    var isFavorite: Int? = 0

)