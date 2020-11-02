package com.dhimas.dhiflix.data.source.remote

class ApiResponse<T>(val statusResponse: StatusResponse, val data: T, val message: String?) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(StatusResponse.SUCCESS, data, null)

        fun <T> empty(data: T, msg: String): ApiResponse<T> =
            ApiResponse(StatusResponse.EMPTY, data, msg)

        fun <T> error(data: T, msg: String): ApiResponse<T> =
            ApiResponse(StatusResponse.ERROR, data, msg)
    }
}