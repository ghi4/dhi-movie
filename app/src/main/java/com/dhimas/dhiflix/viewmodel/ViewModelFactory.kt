package com.dhimas.dhiflix.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.dhimas.dhiflix.data.ShowRepository

class ViewModelFactory private constructor(private val showRepository: ShowRepository): ViewModelProvider.NewInstanceFactory(){

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory =
                instance ?: synchronized(this) {
                    instance ?: ViewModelFactory()
                }
    }
}