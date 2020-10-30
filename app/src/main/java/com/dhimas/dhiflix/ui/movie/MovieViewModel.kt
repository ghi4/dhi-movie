package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var movieList: LiveData<List<ShowEntity>>? = null
    var isAlreadyShimmer: Boolean = false

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getMovies(): LiveData<List<ShowEntity>> {

        //Prevent re-load when rotating phone
        if (movieList == null) {
            movieList = showRepository.getMovieList()
        }

        return movieList as LiveData<List<ShowEntity>>
    }

}