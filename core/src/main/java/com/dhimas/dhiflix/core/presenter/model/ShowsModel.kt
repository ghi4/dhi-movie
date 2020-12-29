package com.dhimas.dhiflix.core.presenter.model

data class ShowsModel(
    var id: String,

    var title: String,

    var releaseDate: String,

    var posterPath: String,

    var backdropPath: String,

    var showType: Int
)