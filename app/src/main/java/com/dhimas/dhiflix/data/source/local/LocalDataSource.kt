package com.dhimas.dhiflix.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.dhimas.dhiflix.data.source.local.entity.SearchShowEntity
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.local.entity.SimilarShowEntity
import com.dhimas.dhiflix.data.source.local.room.ShowDao

class LocalDataSource private constructor(private val showDao: ShowDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(showDao: ShowDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(showDao)
    }

    fun getAllMovie(page: Int): DataSource.Factory<Int, ShowEntity> = showDao.getMovies(page)

    fun getAllSeries(page: Int): DataSource.Factory<Int, ShowEntity> = showDao.getSeries(page)

    fun getAllFavoriteMovie(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteMovies()

    fun getAllFavoriteSeries(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteSeries()

    fun getSimilarMovies(): DataSource.Factory<Int, ShowEntity> = showDao.getSimilarMovies()

    fun getSimilarSeries(): DataSource.Factory<Int, ShowEntity> = showDao.getSimilarSeries()

    fun searchMovie(keyword: String): DataSource.Factory<Int, ShowEntity> =
        showDao.searchMovies(keyword)

    fun searchSeries(keyword: String): DataSource.Factory<Int, ShowEntity> =
        showDao.searchSeries(keyword)

    fun getShowById(showId: String): LiveData<ShowEntity> = showDao.getShowById(showId)

    fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    fun insertSimilarShows(shows: List<SimilarShowEntity>) = showDao.insertSimilarShows(shows)

    fun insertSearchShows(shows: List<SearchShowEntity>) = showDao.insertSearchShows(shows)

    fun deleteAllSimilarShow() = showDao.deleteAllSimilar()

    fun deleteAllSearchShow() = showDao.deleteAllSearch()

    fun setFavorite(showEntity: ShowEntity) {
        showEntity.isFavorite = if (showEntity.isFavorite == 0) 1 else 0
        showDao.updateShow(showEntity)
    }
}