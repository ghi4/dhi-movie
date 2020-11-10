package com.dhimas.dhiflix.data.source.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.local.entity.SimilarShowEntity
import com.dhimas.dhiflix.data.source.local.room.ShowDao

class LocalDataSource private constructor(private val showDao: ShowDao) {

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(showDao: ShowDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(showDao)
    }

    fun getAllMovie(): LiveData<List<ShowEntity>> = showDao.getMovies()

    fun getAllSeries(): LiveData<List<ShowEntity>> = showDao.getSeries()

    fun getAllFavoriteMovie(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteMovies()

    fun getAllFavoriteSeries(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteSeries()

    fun getSimilarMovies(): LiveData<List<ShowEntity>> = showDao.getSimilarMovies()

    fun getSimilarSeries(): LiveData<List<ShowEntity>> = showDao.getSimilarSeries()

    fun getShowById(showId: String): LiveData<ShowEntity> = showDao.getShowById(showId)

    fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    fun insertSimilarShows(shows: List<SimilarShowEntity>) = showDao.insertSimilarShows(shows)

    fun updateShow(show: ShowEntity) = showDao.updateShow(show)

    fun deleteShow(show: ShowEntity) = showDao.deleteShow(show)

    fun deleteAllSimilarShow() = showDao.deleteAllSimilar()

    fun setFavorite(showEntity: ShowEntity) {
        showEntity.isFavorite = if(showEntity.isFavorite == 0) 1 else 0
        showDao.updateShow(showEntity)
    }
}