package com.dhimas.dhiflix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.di.Injection
import com.dhimas.dhiflix.ui.detail.DetailViewModel
import com.dhimas.dhiflix.ui.movie.MovieViewModel
import com.dhimas.dhiflix.ui.series.SeriesViewModel

class ViewModelFactory private constructor(private val showRepository: ShowRepository) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(showRepository) as T
            }

            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(showRepository) as T
            }

            modelClass.isAssignableFrom(SeriesViewModel::class.java) -> {
                SeriesViewModel(showRepository) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}