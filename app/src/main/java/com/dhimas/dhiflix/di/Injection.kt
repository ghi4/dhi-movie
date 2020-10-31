package com.dhimas.dhiflix.di

import android.content.Context
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.LocalDataSource
import com.dhimas.dhiflix.data.source.local.room.ShowDatabase
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitFactory
import com.dhimas.dhiflix.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): ShowRepository {

        val database = ShowDatabase.getInstance(context)
        val retrofitService = RetrofitFactory.makeRetrofitService()

        val remoteDataSource = RemoteDataSource.getInstance(retrofitService)
        val localDataSource = LocalDataSource.getInstance(database.showDao())
        val appExecutors = AppExecutors()

        return ShowRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }
}