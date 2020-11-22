package com.dhimas.dhiflix.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchshowtable")
data class SearchShowEntity(

    @PrimaryKey
    @NonNull
    var id: String,

    var title: String? = null,

    var releaseDate: String? = null,

    var overview: String? = null,

    var posterPath: String? = null,

    var backdropPath: String? = null,

    var showType: Int? = null,

    var isFavorite: Int? = 0

)