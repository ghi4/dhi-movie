package com.dhimas.dhiflix.core.domain.repository

import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import kotlinx.coroutines.flow.Flow

interface IShowRepository {

    fun getMovieList(page: Int): Flow<Resource<List<Show>>>

    fun getSeriesList(page: Int): Flow<Resource<List<Show>>>

    fun getMovieDetail(movieId: String): Flow<Resource<Show>>

    fun getSeriesDetail(seriesId: String): Flow<Resource<Show>>

    fun getFavoriteMovieList(): Flow<Resource<List<Show>>>

    fun getFavoriteSeriesList(): Flow<Resource<List<Show>>>

    fun getSimilarMovieList(movieId: String): Flow<Resource<List<Show>>>

    fun getSimilarSeriesList(seriesId: String): Flow<Resource<List<Show>>>

    fun searchMovie(keyword: String): Flow<Resource<List<Show>>>

    fun searchSeries(keyword: String): Flow<Resource<List<Show>>>

    fun setFavorite(show: Show)
}