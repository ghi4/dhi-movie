package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.DummyData

class DetailViewModel : ViewModel() {
    private var showDetail = ShowEntity()
    private var movieList = ArrayList<ShowEntity>()
    private var seriesList = ArrayList<ShowEntity>()

    fun getShowEntityByTitle(title: String): ShowEntity {

        //Load data when showDetail is empty
        //Data not load again when phone rotating
        if (showDetail.title.isNullOrEmpty()) {
            val movies = DummyData.generateDummyMovies()
            val series = DummyData.generateDummySeries()
            val showList = ArrayList<ShowEntity>()

            showList.addAll(movies)
            showList.addAll(series)

            //Get showDetail by its title name
            val show = showList.filter { it.title == title }
            showDetail = show[0]
        }

        return showDetail
    }

    fun getMovieButExclude(showEntity: ShowEntity): ArrayList<ShowEntity> {

        //Load data when movieList is empty
        //Data not load again when phone rotating
        if (movieList.isEmpty()) {
            val movies = DummyData.generateDummyMovies()
            movies.removeAll(listOf(showEntity))

            movieList.addAll(movies)
        }

        return movieList
    }

    fun getSeriesButExclude(showEntity: ShowEntity): ArrayList<ShowEntity> {

        //Load data when seriesList is empty
        //Data not load again when phone rotating
        if (seriesList.isEmpty()) {
            val series = DummyData.generateDummySeries()
            series.removeAll(listOf(showEntity))

            seriesList.addAll(series)
        }

        return seriesList
    }
}