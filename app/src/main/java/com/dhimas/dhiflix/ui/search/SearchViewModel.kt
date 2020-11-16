package com.dhimas.dhiflix.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()

    private var movieList: LiveData<Resource<PagedList<ShowEntity>>> =
        Transformations.switchMap(searchQuery) { mSearchQuery ->
            Log.d("Kucingy", "MOV: " + searchQuery.value.toString())
            showRepository.searchMovie(mSearchQuery)
        }

    private var seriesList: LiveData<Resource<PagedList<ShowEntity>>> =
        Transformations.switchMap(searchQuery) { mSearchQuery ->
            Log.d("Kucingy", "SER: " + searchQuery.value.toString())
            showRepository.searchSeries(mSearchQuery)
        }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.postValue(searchQuery)
    }

    fun getSeries() = seriesList

    fun getMovies() = movieList
}