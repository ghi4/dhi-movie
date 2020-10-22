package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import com.dhimas.dhiflix.data.source.local.ShowEntity

interface ShowDataSource {

    fun getMovieList(): LiveData<List<ShowEntity>>

    fun getMovieDetail(movie_id: String): LiveData<ShowEntity>

    fun getSeriesList(): LiveData<List<ShowEntity>>

    fun getSeriesDetail(series_id: String): LiveData<ShowEntity>

}