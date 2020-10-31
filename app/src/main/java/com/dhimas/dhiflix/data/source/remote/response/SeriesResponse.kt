package com.dhimas.dhiflix.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SeriesResponse(

    @SerializedName("id")
    var series_id: String,

    @SerializedName("name")
    var name: String,

    @SerializedName("first_air_date")
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