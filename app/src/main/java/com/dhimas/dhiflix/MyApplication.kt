@file:Suppress("unused") //This file used for Koin Dependency Injection

package com.dhimas.dhiflix

import android.app.Application
import com.dhimas.dhiflix.core.di.databaseModule
import com.dhimas.dhiflix.core.di.networkModule
import com.dhimas.dhiflix.core.di.repositoryModule
import com.dhimas.dhiflix.di.useCaseModule
import com.dhimas.dhiflix.di.viewModelModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    viewModelModule
                )
            )
        }
    }
}