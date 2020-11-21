package com.dhimas.dhiflix.ui.search.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchMovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()

    private var movieList = searchQuery.switchMap {
        showRepository.searchMovie(it.toString())
    }

    fun setSearch(search: String) {
        searchQuery.postValue(search)
    }

    fun getMovies(): LiveData<Resource<List<ShowEntity>>> = movieList

    fun getSearch() = searchQuery
}