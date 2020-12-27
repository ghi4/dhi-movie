package com.dhimas.dhiflix.ui.movie

import android.util.Log
import androidx.lifecycle.*
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class MovieViewModel(showUseCase: ShowUseCase) : ViewModel() {
    private var isAlreadyShimmer: Boolean = false
    private val page = MutableLiveData<Int>()

    private var movieList = page.switchMap {
        showUseCase.getMovieList(it).asLiveData()
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

    override fun onCleared() {
        super.onCleared()

        Log.d("KEPOO", "MOVIE - ViewModel - Cleared")
    }

}