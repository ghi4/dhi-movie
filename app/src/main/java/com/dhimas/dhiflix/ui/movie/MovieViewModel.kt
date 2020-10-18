package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class MovieViewModel : ViewModel() {

    fun getMovies(): ArrayList<MovieEntity> = DummyData.generateDummyMovies()
}