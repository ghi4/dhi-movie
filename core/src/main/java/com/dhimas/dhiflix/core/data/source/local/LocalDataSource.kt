package com.dhimas.dhiflix.core.data.source.local

import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.local.room.ShowDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val showDao: ShowDao) {

    fun getMovies(page: Int): Flow<List<ShowEntity>> = showDao.getMovies(page)

    fun getSeries(page: Int): Flow<List<ShowEntity>> = showDao.getSeries(page)

    fun getFavoriteMovies(): Flow<List<ShowEntity>> = showDao.getFavoriteMovies()

    fun getFavoriteSeries(): Flow<List<ShowEntity>> = showDao.getFavoriteSeries()

    fun getSearchMovies(keyword: String): Flow<List<ShowEntity>> = showDao.searchMovies(keyword)

    fun getSearchSeries(keyword: String): Flow<List<ShowEntity>> = showDao.searchSeries(keyword)

    fun getShowById(showId: String): Flow<ShowEntity> = showDao.getShowById(showId)

    suspend fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    suspend fun deleteAllSearchShow(showType: Int) = showDao.deleteAllSearch(showType)

    suspend fun setFavorite(showEntity: ShowEntity) = showDao.updateShow(showEntity)

}