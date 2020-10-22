package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity

class MovieViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun getMovies(): LiveData<List<ShowEntity>> = showRepository.getMovieList()

}