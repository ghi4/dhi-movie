package com.dhimas.dhiflix.core.domain.usecase

import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface ShowUseCase {
    fun getMovieList(page: Int): Flow<Resource<List<Show>>>

    fun getSeriesList(page: Int): Flow<Resource<List<Show>>>

    fun getMovieDetail(movie_id: String): Flow<Resource<Show>>

    fun getSeriesDetail(series_id: String): Flow<Resource<Show>>

    fun getFavoriteMovieList(): Flow<Resource<List<Show>>>

    fun getFavoriteSeriesList(): Flow<Resource<List<Show>>>

    fun getSimilarMovieList(movie_id: String): Flow<Resource<List<Show>>>

    fun getSimilarSeriesList(series_id: String): Flow<Resource<List<Show>>>

    fun searchMovie(keyword: String): Flow<Resource<List<Show>>>

    fun searchSeries(keyword: String): Flow<Resource<List<Show>>>

    fun setFavorite(show: Show)
}