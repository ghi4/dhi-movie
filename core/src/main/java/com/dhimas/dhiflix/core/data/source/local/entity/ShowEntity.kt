package com.dhimas.dhiflix.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "showtable")
data class ShowEntity(

    @PrimaryKey
    @NonNull
    var id: String,

    var title: String? = "Unknown",

    var releaseDate: String? = "Unknown",

    var overview: String? = "Unknown",

    var posterPath: String? = "Unknown",

    var backdropPath: String? = "Unknown",

    var showType: Int? = 0,

    var isFavorite: Int? = 0,

    var isSimilar: Int? = 0,

    var isSearch: Int? = 0

)