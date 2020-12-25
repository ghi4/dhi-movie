package com.dhimas.dhiflix.core.utils

import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors @VisibleForTesting constructor(
    private val diskIO: Executor,
    private val mainThread: Executor
) {

    constructor() : this(
        Executors.newSingleThreadExecutor(),
        MainThreadExecutor()
    )

    fun diskIO(): Executor = diskIO

    fun mainThread(): Executor = mainThread

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = android.os.Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}