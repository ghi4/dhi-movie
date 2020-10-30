package com.dhimas.dhiflix.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant

@Dao
interface ShowDao {

    @Query("SELECT * FROM showtable WHERE show_type = ${Constant.MOVIE_TYPE}")
    fun getMovies(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE show_type = ${Constant.SERIES_TYPE}")
    fun getSeries(): LiveData<List<ShowEntity>>

    @Query("SELECT * FROM showtable WHERE id = :showId")
    fun getShowById(showId: String): LiveData<ShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<ShowEntity>)

    @Update
    fun updateShow(show: ShowEntity)

    @Delete
    fun deleteShow(show: ShowEntity)
}