package com.dhimas.dhiflix.vo

class ApiResponse<T>(val status: Status, val data: T, val message: String?) {

    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(Status.SUCCESS, data, null)

        fun <T> loading(data: T): ApiResponse<T> = ApiResponse(Status.LOADING, data, null)

        fun <T> error(data: T, msg: String): ApiResponse<T> = ApiResponse(Status.ERROR, data, msg)
    }
}