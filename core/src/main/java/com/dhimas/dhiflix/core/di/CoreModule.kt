package com.dhimas.dhiflix.core.di

import androidx.room.Room
import com.dhimas.dhiflix.core.data.ShowRepository
import com.dhimas.dhiflix.core.data.source.local.LocalDataSource
import com.dhimas.dhiflix.core.data.source.local.room.ShowDatabase
import com.dhimas.dhiflix.core.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.core.data.source.remote.retrofit.RetrofitInterface
import com.dhimas.dhiflix.core.domain.repository.IShowRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<ShowDatabase>().showDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            ShowDatabase::class.java,
            "Shows.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RetrofitInterface::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<IShowRepository> { ShowRepository(get(), get()) }
}