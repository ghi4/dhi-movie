package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()
    private var triggerSeries = MutableLiveData<Unit>()
    private var triggerMovie = MutableLiveData<Unit>()

    private var movieList =
        triggerMovie.switchMap {
            showRepository.searchMovie(searchQuery.value.toString())
        }

    private var seriesList =
        triggerSeries.switchMap {
            showRepository.searchSeries(searchQuery.value.toString())
        }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.postValue(searchQuery)
    }

    fun getSearchQuery() = searchQuery

    fun getSeries(): LiveData<Resource<List<ShowEntity>>> = seriesList

    fun getMovies(): LiveData<Resource<List<ShowEntity>>> = movieList

    fun triggerMovie(){
        triggerMovie.postValue(Unit)
    }

    fun triggerSeries(){
        triggerSeries.postValue(Unit)
    }
}