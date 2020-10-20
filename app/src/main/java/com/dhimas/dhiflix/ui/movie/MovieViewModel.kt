package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowEntity
import com.dhimas.dhiflix.utils.DummyData

class MovieViewModel : ViewModel() {
    private var movieList = ArrayList<ShowEntity>()

    fun getMovies(): ArrayList<ShowEntity> {

        //Load data when moviesList is empty
        //Prevent re-load when rotating phone
        if (movieList.isEmpty()) {
            val movies = DummyData.generateDummyMovies()
            movieList.addAll(movies)
        }

        return movieList
    }
}