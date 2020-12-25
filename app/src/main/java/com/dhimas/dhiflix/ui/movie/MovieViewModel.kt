package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class MovieViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    private var isAlreadyShimmer: Boolean = false
    private val page = MutableLiveData<Int>()

    private var movieList = page.switchMap {
        showUseCase.getMovieList(it)
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setPage(page: Int) {
        this.page.postValue(page)
    }

    fun getIsAlreadyShimmer() = isAlreadyShimmer

    fun getMovies(): LiveData<Resource<List<Show>>> = movieList

    fun refresh() {
        page.postValue(page.value)
    }

}