package com.dhimas.dhiflix.data.source.local

import androidx.lifecycle.LiveData
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.local.room.ShowDao

class LocalDataSource private constructor(private val showDao: ShowDao){

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(showDao: ShowDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(showDao)
    }

    fun getAllMovie(): LiveData<List<ShowEntity>> = showDao.getMovies()

    fun getAllSeries(): LiveData<List<ShowEntity>> = showDao.getMovies()

    fun getShowById(showId: String): LiveData<ShowEntity> = showDao.getShowById(showId)

    fun insertShows(shows: List<ShowEntity>) = showDao.insertShows(shows)

    fun updateShow(show: ShowEntity) = showDao.updateShow(show)

    fun deleteShow(show: ShowEntity) = showDao.deleteShow(show)
}