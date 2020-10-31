package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

interface ShowDataSource {

    fun getMovieList(): LiveData<Resource<List<ShowEntity>>>

    fun getSeriesList(): LiveData<Resource<List<ShowEntity>>>

    fun getMovieDetail(movie_id: String): LiveData<Resource<ShowEntity>>

    fun getSeriesDetail(series_id: String): LiveData<Resource<ShowEntity>>

    fun getFavoriteMovieList(): LiveData<Resource<PagedList<ShowEntity>>>

    fun getFavoriteSeriesList(): LiveData<Resource<PagedList<ShowEntity>>>

}