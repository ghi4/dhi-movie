package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private lateinit var movieList: LiveData<Resource<PagedList<ShowEntity>>>

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getMovies(): LiveData<Resource<PagedList<ShowEntity>>> {
        if (!::movieList.isInitialized)
            movieList = showRepository.getMovieList()

        return movieList
    }

    fun refresh() {
        movieList = showRepository.getMovieList()
    }

}