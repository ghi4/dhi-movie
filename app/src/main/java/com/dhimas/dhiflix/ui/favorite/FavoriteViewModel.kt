package com.dhimas.dhiflix.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class FavoriteViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    private val refreshTrigger = MutableLiveData(Unit)

    private var movieList = refreshTrigger.switchMap {
        showUseCase.getFavoriteMovieList()
    }

    private var seriesList = refreshTrigger.switchMap {
        showUseCase.getFavoriteSeriesList()
    }

    fun getFavoriteMovies(): LiveData<Resource<List<Show>>> = movieList

    fun getFavoriteSeries(): LiveData<Resource<List<Show>>> = seriesList

    fun refresh() {
        refreshTrigger.value = Unit
    }
}