package com.dhimas.dhiflix.core.data.source.local

import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.local.room.ShowDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val showDao: ShowDao) {

    fun getMovies(page: Int): Flow<List<ShowEntity>> = showDao.getMovies(page)

    fun getSeries(page: Int): Flow<List<ShowEntity>> = showDao.getSeries(page)

    fun getFavoriteMovies(): Flow<List<ShowEntity>> = showDao.getFavoriteMovies()

    fun getFavoriteSeries(): Flow<List<ShowEntity>> = showDao.getFavoriteSeries()

    fun getSimilarMovies(): Flow<List<ShowEntity>> = showDao.getSimilarMovies()

    fun getSimilarSeries(): Flow<List<ShowEntity>> = showDao.getSimilarSeries()

    fun searchMovies(keyword: String): Flow<List<ShowEntity>> = showDao.searchMovies(keyword)

    fun searchSeries(keyword: String): Flow<List<ShowEntity>> = showDao.searchSeries(keyword)

    fun getShowById(showId: String): Flow<ShowEntity> = showDao.getShowById(showId)

    suspend fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    //fun deleteAllSimilarShow(showType: Int) = showDao.deleteAllSimilar(showType)

    //fun deleteAllSearchShow(showType: Int) = showDao.deleteAllSearch(showType)

    fun setFavorite(showEntity: ShowEntity) {
        showEntity.isFavorite = if (showEntity.isFavorite == 0) 1 else 0
        showEntity.page = 0
        showDao.updateShow(showEntity)
    }
}