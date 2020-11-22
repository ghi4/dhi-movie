package com.dhimas.dhiflix.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.dhimas.dhiflix.data.source.local.entity.SearchShowEntity
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.local.entity.SimilarShowEntity
import com.dhimas.dhiflix.utils.Constant

@Dao
interface ShowDao {

    @Query("SELECT * FROM showtable WHERE showType = ${Constant.MOVIE_TYPE} AND page BETWEEN 1 AND :page")
    fun getMovies(page: Int): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Constant.SERIES_TYPE} AND page BETWEEN 1 AND :page")
    fun getSeries(page: Int): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE showType = ${Constant.MOVIE_TYPE} AND isFavorite = 1")
    fun getFavoriteMovies(): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM showtable WHERE showType = ${Constant.SERIES_TYPE} AND isFavorite = 1")
    fun getFavoriteSeries(): DataSource.Factory<Int, ShowEntity>

    @Query("SELECT * FROM similarshowtable WHERE showType = ${Constant.MOVIE_TYPE}")
    fun getSimilarMovies(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM similarshowtable WHERE showType = ${Constant.SERIES_TYPE}")
    fun getSimilarSeries(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM searchshowtable WHERE showType = ${Constant.MOVIE_TYPE} AND title LIKE :keyword")
    fun searchMovies(keyword: String): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM searchshowtable WHERE showType = ${Constant.SERIES_TYPE} AND title LIKE :keyword")
    fun searchSeries(keyword: String): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE id = :showId")
    fun getShowById(showId: String): LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<ShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSimilarShows(shows: List<SimilarShowEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSearchShows(shows: List<SearchShowEntity>)

    @Update
    fun updateShow(show: ShowEntity)

    @Query("DELETE FROM similarshowtable WHERE showType = :showType")
    fun deleteAllSimilar(showType: Int)

    @Query("DELETE FROM searchshowtable WHERE showType = :showType")
    fun deleteAllSearch(showType: Int)
}