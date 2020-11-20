package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

interface ShowDataSource {

    fun getMovieList(page: Int): LiveData<Resource<PagedList<ShowEntity>>>

    fun getSeriesList(page: Int): LiveData<Resource<PagedList<ShowEntity>>>

    fun getMovieDetail(movie_id: String): LiveData<Resource<ShowEntity>>

    fun getSeriesDetail(series_id: String): LiveData<Resource<ShowEntity>>

    fun getFavoriteMovieList(): LiveData<Resource<PagedList<ShowEntity>>>

    fun getFavoriteSeriesList(): LiveData<Resource<PagedList<ShowEntity>>>

    fun getSimilarMovieList(movie_id: String): LiveData<Resource<PagedList<ShowEntity>>>

    fun getSimilarSeriesList(series_id: String): LiveData<Resource<PagedList<ShowEntity>>>

    fun searchMovie(keyword: String): LiveData<Resource<PagedList<ShowEntity>>>

    fun searchSeries(keyword: String): LiveData<Resource<PagedList<ShowEntity>>>

}