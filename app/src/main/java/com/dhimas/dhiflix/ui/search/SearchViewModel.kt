package com.dhimas.dhiflix.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()

    private var movieList =
        searchQuery.switchMap {
            Log.d("GGT", "IN MOV")
            showRepository.searchMovie(it)
        }

    private var seriesList =
        searchQuery.switchMap {
            Log.d("GGT", "IN SER")
            showRepository.searchSeries(it)
        }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.postValue(searchQuery)
    }

    fun getSeries(): LiveData<Resource<List<ShowEntity>>> = seriesList

    fun getMovies(): LiveData<Resource<List<ShowEntity>>> = movieList

}