package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private val refreshTrigger = MutableLiveData(Unit)
    private var movieList = refreshTrigger.switchMap {
        showRepository.getMovieList()
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>> = movieList

    fun refresh() {
        refreshTrigger.value = Unit
    }

}