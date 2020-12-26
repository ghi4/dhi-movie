package com.dhimas.dhiflix.core.data.source.remote.retrofit

import com.dhimas.dhiflix.core.BuildConfig
import com.dhimas.dhiflix.core.data.source.remote.response.MovieListResponse
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesListResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("movie/popular")
    suspend fun getMovieList(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int
    ): MovieListResponse

    @GET("tv/popular")
    suspend fun getSeriesList(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int
    ): SeriesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): MovieResponse

    @GET("tv/{tv_id}")
    suspend fun getSeriesDetail(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): SeriesResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): MovieListResponse

    @GET("tv/{tv_id}/similar")
    suspend fun getSimilarSeries(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): SeriesListResponse

    @GET("/3/search/movie")
    suspend fun searchMovie(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("query") keyword: String
    ): MovieListResponse

    @GET("/3/search/tv")
    suspend fun searchSeries(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("query") keyword: String
    ): SeriesListResponse
}