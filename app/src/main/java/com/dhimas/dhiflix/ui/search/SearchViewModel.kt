package com.dhimas.dhiflix.ui.search

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
            showRepository.searchMovie(mSearchQuery)
        }

    private var seriesList: LiveData<Resource<PagedList<ShowEntity>>> =
        Transformations.switchMap(searchQuery) { mSearchQuery ->
            showRepository.searchSeries(mSearchQuery)
        }

    fun setSearchQuery(searchQuery: String) {
        this.searchQuery.value = searchQuery
    }

    fun getSeries() = seriesList

    fun getMovies() = movieList
}