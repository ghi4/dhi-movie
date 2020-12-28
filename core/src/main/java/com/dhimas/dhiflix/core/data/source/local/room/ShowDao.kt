package com.dhimas.dhiflix.core.data.source.local.room

import androidx.room.*
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.utils.Const
import kotlinx.coroutines.flow.Flow

@Dao
interface ShowDao {

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isSearch = 0 AND isSimilar = 0 LIMIT :page")
    fun getMovies(page: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isSearch = 0 AND isSimilar = 0 LIMIT :page")
    fun getSeries(page: Int): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isFavorite = 1")
    fun getFavoriteMovies(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isFavorite = 1")
    fun getFavoriteSeries(): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND isSimilar = 1  AND isFavorite = 0 AND id NOT IN (:movieId)")
    fun getSimilarMovies(movieId: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND isSimilar = 1 AND isFavorite = 0 AND id NOT IN (:seriesId)")
    fun getSimilarSeries(seriesId: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.MOVIE_TYPE} AND  title LIKE :keyword")
    fun searchMovies(keyword: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Const.SERIES_TYPE} AND title LIKE :keyword")
    fun searchSeries(keyword: String): Flow<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE id = :showId")
    fun getShowById(showId: String): Flow<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShows(shows: List<ShowEntity>)

    @Update
    fun updateShow(show: ShowEntity)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSimilar = 1 AND isFavorite = 0 AND id NOT IN (:showId)")
    suspend fun deleteSimilarExcept(showId: String, showType: Int)

    @Query("DELETE FROM showtable WHERE showType = :showType AND isSearch = 1 AND isFavorite = 0")
    suspend fun deleteAllSearch(showType: Int)
}