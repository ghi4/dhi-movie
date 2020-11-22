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

    fun getMovies(page: Int): LiveData<List<ShowEntity>> = showDao.getMovies(page)

    fun getSeries(page: Int): LiveData<List<ShowEntity>> = showDao.getSeries(page)

    fun getFavoriteMovies(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteMovies()

    fun getFavoriteSeries(): DataSource.Factory<Int, ShowEntity> = showDao.getFavoriteSeries()

    fun getSimilarMovies(): DataSource.Factory<Int, ShowEntity> = showDao.getSimilarMovies()

    fun getSimilarSeries(): DataSource.Factory<Int, ShowEntity> = showDao.getSimilarSeries()

    fun searchMovies(keyword: String): LiveData<List<ShowEntity>> = showDao.searchMovies(keyword)

    fun searchSeries(keyword: String): LiveData<List<ShowEntity>> = showDao.searchSeries(keyword)

    fun getShowById(showId: String): LiveData<ShowEntity> = showDao.getShowById(showId)

    fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    fun insertSimilarShows(shows: List<SimilarShowEntity>) = showDao.insertSimilarShows(shows)

    fun insertSearchShows(shows: List<SearchShowEntity>) = showDao.insertSearchShows(shows)

    fun deleteAllSimilarShow(showType: Int) = showDao.deleteAllSimilar(showType)

    fun deleteAllSearchShow(showType: Int) = showDao.deleteAllSearch(showType)

    fun setFavorite(showEntity: ShowEntity) {
        showEntity.isFavorite = if (showEntity.isFavorite == 0) 1 else 0
        showDao.updateShow(showEntity)
    }
}