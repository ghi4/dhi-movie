package com.dhimas.dhiflix.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Const

@Dao
interface ShowDao {

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND page BETWEEN 1 AND :page")
    fun getMovies(page: Int): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND page BETWEEN 1 AND :page")
    fun getSeries(page: Int): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isFavorite = 1")
    fun getFavoriteMovies(): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isFavorite = 1")
    fun getFavoriteSeries(): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isSimilar = 1")
    fun getSimilarMovies(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isSimilar = 1")
    fun getSimilarSeries(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND title LIKE :keyword AND isSearch = 1")
    fun searchMovies(keyword: String): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND title LIKE :keyword AND isSearch = 1")
    fun searchSeries(keyword: String): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE id = :showId AND isSimilar = 0")
    fun getShowById(showId: String): LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<ShowEntity>)

    @Update
    fun updateShow(show: ShowEntity)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSimilar = 1 AND isFavorite = 0")
    fun deleteAllSimilar(showType: Int)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSearch = 1 AND isFavorite = 0")
    fun deleteAllSearch(showType: Int)
}