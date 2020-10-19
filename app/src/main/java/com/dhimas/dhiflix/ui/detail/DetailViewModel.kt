package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class DetailViewModel: ViewModel() {

    fun getMovieButExclude(movieEntity: MovieEntity): ArrayList<MovieEntity> {
        val movies = DummyData.generateDummyMovies()
        movies.removeAll(listOf(movieEntity))

        return movies
    }

    fun getSeriesButExclude(movieEntity: MovieEntity): ArrayList<MovieEntity> {
        val series = DummyData.generateDummySeries()
        series.removeAll(listOf(movieEntity))

        return series
    }
}