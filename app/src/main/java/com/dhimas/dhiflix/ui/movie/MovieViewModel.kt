package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getMovies(): LiveData<Resource<List<ShowEntity>>> = showRepository.getMovieList()

}