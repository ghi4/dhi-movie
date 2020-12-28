package com.dhimas.dhiflix.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dhimas.dhiflix.core.utils.Const

@Entity(tableName = "showtable")
data class ShowEntity(

    @PrimaryKey
    @NonNull
    var id: String,

    var title: String? = Const.UNKNOWN_VALUE,

    var releaseDate: String? = Const.UNKNOWN_VALUE,

    var overview: String? = Const.UNKNOWN_VALUE,

    var posterPath: String? = Const.UNKNOWN_VALUE,

    var backdropPath: String? = Const.UNKNOWN_VALUE,

    var showType: Int? = 0,

    var isFavorite: Int? = 0,

    var isSimilar: Int? = 0,

    var isSearch: Int? = 0

)