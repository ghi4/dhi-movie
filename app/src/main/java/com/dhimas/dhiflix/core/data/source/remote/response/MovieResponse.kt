package com.dhimas.dhiflix.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieResponse(

    @SerializedName("id")
    var movie_id: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("release_date")
    var releaseDate: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("poster_path")
    var posterPath: String,

    @SerializedName("backdrop_path")
    var backdropPath: String

) {
    constructor() : this("", "", "", "", "", "")
}