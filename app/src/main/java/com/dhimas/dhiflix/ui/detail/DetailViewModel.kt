package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity

class DetailViewModel: ViewModel() {
    private var movieEntity = MovieEntity()

    fun setMovie(movieEntity: MovieEntity){
        this.movieEntity = movieEntity
    }

    fun getMovie() = this.movieEntity
}