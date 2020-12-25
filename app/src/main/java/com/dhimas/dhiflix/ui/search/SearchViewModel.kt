package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class SearchViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()

    private var movieList =
        searchQuery.switchMap {
            showUseCase.searchMovie(it)
        }

    private var seriesList =
        searchQuery.switchMap {
            showUseCase.searchSeries(it)
        }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.postValue(searchQuery)
    }

    fun getSeries(): LiveData<Resource<List<Show>>> = seriesList

    fun getMovies(): LiveData<Resource<List<Show>>> = movieList

}