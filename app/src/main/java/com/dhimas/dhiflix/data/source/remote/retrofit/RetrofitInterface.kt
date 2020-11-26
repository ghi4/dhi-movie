package com.dhimas.dhiflix.data.source.remote.retrofit

import com.dhimas.dhiflix.BuildConfig
import com.dhimas.dhiflix.data.source.remote.response.MovieListResponse
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesListResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitInterface {

    @GET("movie/popular")
    fun getMovieList(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int
    ): Call<MovieListResponse>

    @GET("tv/popular")
    fun getSeriesList(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("page") page: Int
    ): Call<SeriesListResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): Call<MovieResponse>

    @GET("tv/{tv_id}")
    fun getSeriesDetail(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): Call<SeriesResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): Call<MovieListResponse>

    @GET("tv/{tv_id}/similar")
    fun getSimilarSeries(
        @Path("tv_id") tvId: String,
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY
    ): Call<SeriesListResponse>

    @GET("/3/search/movie")
    fun searchMovie(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("query") keyword: String
    ): Call<MovieListResponse>

    @GET("/3/search/tv")
    fun searchSeries(
        @Query("api_key") api: String? = BuildConfig.TMDB_API_KEY,
        @Query("query") keyword: String
    ): Call<SeriesListResponse>
}