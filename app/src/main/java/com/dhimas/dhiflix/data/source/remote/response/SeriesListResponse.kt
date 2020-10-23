package com.dhimas.dhiflix.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SeriesListResponse(
    @SerializedName("results")
    val seriesList: List<SeriesResponse>? = null
)