package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var movieList: LiveData<List<ShowEntity>>? = null

    fun getMovies(): LiveData<List<ShowEntity>> {
        if (movieList == null) {
            movieList = showRepository.getMovieList()
        }

        return movieList as LiveData<List<ShowEntity>>
    }

}