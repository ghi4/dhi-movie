package com.dhimas.dhiflix.data.source.remote.retrofit

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitFactory {
    companion object{
        private const val BASE_URL = "https://api.themoviedb.org/3/"

        @Provides
        fun makeRetrofitService(): RetrofitInterface {
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(RetrofitInterface::class.java)
        }

    }
}