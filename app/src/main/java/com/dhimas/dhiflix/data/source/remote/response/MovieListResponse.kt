package com.dhimas.dhiflix.data.source.remote.response

import com.google.gson.annotations.SerializedName

class MovieListResponse {

    @SerializedName("results")
    val showList: List<MovieResponse>? = null

}