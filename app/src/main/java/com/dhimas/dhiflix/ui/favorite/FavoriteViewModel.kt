package com.dhimas.dhiflix.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class FavoriteViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private val refreshTrigger = MutableLiveData(Unit)

    private var movieList = refreshTrigger.switchMap {
        showRepository.getFavoriteMovieList()
    }

    private var seriesList = refreshTrigger.switchMap {
        showRepository.getFavoriteSeriesList()
    }

    fun getFavoriteMovies(): LiveData<Resource<PagedList<ShowEntity>>> = movieList

    fun getFavoriteSeries(): LiveData<Resource<PagedList<ShowEntity>>> = seriesList

    fun refresh() {
        refreshTrigger.value = Unit
    }
}