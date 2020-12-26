package com.dhimas.dhiflix.core.domain.model

data class Show(
    var id: String,

    var title: String? = null,

    var releaseDate: String? = null,

    var overview: String? = null,

    var posterPath: String? = null,

    var backdropPath: String? = null,

    var showType: Int? = 0,

    var isFavorite: Int? = 0,

    var isSimilar: Int? = 0,

    var isSearch: Int? = 0
)
