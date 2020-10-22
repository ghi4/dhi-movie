package com.dhimas.dhiflix.data.source.remote.response

import com.google.gson.annotations.SerializedName

class ShowListResponse {

    @SerializedName("results")
    val showList: List<ShowResponse>? = null

}