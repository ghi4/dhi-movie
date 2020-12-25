package com.dhimas.dhiflix.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MovieListResponse(

    @SerializedName("results")
    val movieList: List<MovieResponse>

)