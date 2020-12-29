package com.dhimas.dhiflix.core.presenter.model

data class ShowsDetailModel(

    var title: String,

    var releaseDate: String,

    var overview: String,

    var posterPath: String,

    var backdropPath: String,

    var isFavorite: Int
)