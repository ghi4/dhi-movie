package com.dhimas.dhiflix.di

import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitFactory

object Injection {
    fun provideRepository(): ShowRepository {

        val remoteRepository = RemoteDataSource.getInstance(RetrofitFactory.makeRetrofitService())

        return ShowRepository.getInstance(remoteRepository)
    }
}