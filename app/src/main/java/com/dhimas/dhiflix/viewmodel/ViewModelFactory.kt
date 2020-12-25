package com.dhimas.dhiflix.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dhimas.dhiflix.core.data.ShowRepository
import com.dhimas.dhiflix.core.di.Injection
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import com.dhimas.dhiflix.ui.detail.DetailViewModel
import com.dhimas.dhiflix.ui.favorite.FavoriteViewModel
import com.dhimas.dhiflix.ui.movie.MovieViewModel
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesViewModel

class ViewModelFactory private constructor(private val showUseCase: ShowUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideShowUseCase(context))
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(showUseCase) as T
            }

            modelClass.isAssignableFrom(MovieViewModel::class.java) -> {
                MovieViewModel(showUseCase) as T
            }

            modelClass.isAssignableFrom(SeriesViewModel::class.java) -> {
                SeriesViewModel(showUseCase) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(showUseCase) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(showUseCase) as T
            }

            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
    }
}