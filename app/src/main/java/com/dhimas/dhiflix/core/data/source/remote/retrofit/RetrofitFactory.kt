package com.dhimas.dhiflix.core.data.source.remote.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun makeRetrofitService(): RetrofitInterface {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RetrofitInterface::class.java)
        }

    }
}