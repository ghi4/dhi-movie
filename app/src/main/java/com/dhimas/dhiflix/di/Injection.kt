package com.dhimas.dhiflix.di

import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitFactory

object Injection {

    fun provideRepository(): ShowRepository {
        val retrofitService = RetrofitFactory.makeRetrofitService()
        val remoteRepository = RemoteDataSource.getInstance(retrofitService)

        return ShowRepository.getInstance(remoteRepository)
    }
}