package com.dhimas.dhiflix.core.di

import android.content.Context
import com.dhimas.dhiflix.core.data.ShowRepository
import com.dhimas.dhiflix.core.data.source.local.LocalDataSource
import com.dhimas.dhiflix.core.data.source.local.room.ShowDatabase
import com.dhimas.dhiflix.core.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.core.data.source.remote.retrofit.RetrofitFactory
import com.dhimas.dhiflix.core.domain.usecase.ShowInteractor
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import com.dhimas.dhiflix.core.utils.AppExecutors

object Injection {

    fun provideRepository(context: Context): ShowRepository {

        val database = ShowDatabase.getInstance(context)
        val retrofitService = RetrofitFactory.makeRetrofitService()

        val remoteDataSource = RemoteDataSource.getInstance(retrofitService)
        val localDataSource = LocalDataSource.getInstance(database.showDao())
        val appExecutors = AppExecutors()

        return ShowRepository.getInstance(remoteDataSource, localDataSource, appExecutors)
    }

    fun provideShowUseCase(context: Context): ShowUseCase {
        val repository = provideRepository(context)
        return ShowInteractor(repository)
    }
}