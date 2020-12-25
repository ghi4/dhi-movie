package com.dhimas.dhiflix.core.data.source.local.room

import androidx.room.*
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.utils.Const
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND page BETWEEN 1 AND :page")
    fun getMovies(page: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND page BETWEEN 1 AND :page")
    fun getSeries(page: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isFavorite = 1")
    fun getFavoriteSeries(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isSimilar = 1")
    fun getSimilarMovies(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isSimilar = 1")
    fun getSimilarSeries(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND title LIKE :keyword AND isSearch = 1")
    fun searchMovies(keyword: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND title LIKE :keyword AND isSearch = 1")
    fun searchSeries(keyword: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE id = :showId AND isSimilar = 0")
    fun getShowById(showId: String): Flow<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShows(shows: List<ShowEntity>)

    @Update
    fun updateShow(show: ShowEntity)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSimilar = 1 AND isFavorite = 0")
    fun deleteAllSimilar(showType: Int)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSearch = 1 AND isFavorite = 0")
    fun deleteAllSearch(showType: Int)
}