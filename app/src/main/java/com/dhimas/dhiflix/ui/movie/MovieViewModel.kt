package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.*
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class MovieViewModel(showUseCase: ShowUseCase) : ViewModel() {
    //Value if already shimmer or not
    private var isAlreadyShimmer: Boolean = false

    //Used for "Load More"
    private val page = MutableLiveData<Int>()

    //Get movie list
    //Triggered when page is set by setPage() or refresh() is called
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

}