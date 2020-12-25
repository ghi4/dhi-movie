package com.dhimas.dhiflix.core.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.data.Resource

interface ShowUseCase {
    fun getMovieList(page: Int): LiveData<Resource<List<Show>>>

    fun getSeriesList(page: Int): LiveData<Resource<List<Show>>>

    fun getMovieDetail(movie_id: String): LiveData<Resource<Show>>

    fun getSeriesDetail(series_id: String): LiveData<Resource<Show>>

    fun getFavoriteMovieList(): LiveData<Resource<List<Show>>>

    fun getFavoriteSeriesList(): LiveData<Resource<List<Show>>>

    fun getSimilarMovieList(movie_id: String): LiveData<Resource<List<Show>>>

    fun getSimilarSeriesList(series_id: String): LiveData<Resource<List<Show>>>

    fun searchMovie(keyword: String): LiveData<Resource<List<Show>>>

    fun searchSeries(keyword: String): LiveData<Resource<List<Show>>>

    fun setFavorite(show: Show)
}