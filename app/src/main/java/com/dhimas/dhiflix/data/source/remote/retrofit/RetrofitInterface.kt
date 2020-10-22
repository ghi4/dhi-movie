package com.dhimas.dhiflix.data.source.remote.retrofit

import com.dhimas.dhiflix.data.source.remote.response.MovieListResponse
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesListResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInterface {

    @GET("movie/popular?api_key=ce041f4e3f3987fb8580b0cf374393a6&language=en-US&page=1")
    fun getMovieList(): Call<MovieListResponse>

    @GET("movie/{movie_id}?api_key=ce041f4e3f3987fb8580b0cf374393a6&language=en-US")
    fun getMovieDetail(@Path("movie_id") movie_id: String?): Call<MovieResponse>

    @GET("tv/popular?api_key=ce041f4e3f3987fb8580b0cf374393a6&language=en-US&page=1")
    fun getSeriesList(): Call<SeriesListResponse>

    @GET("tv/{movie_id}?api_key=ce041f4e3f3987fb8580b0cf374393a6&language=en-US")
    fun getSeriesDetail(@Path("movie_id") movie_id: String?): Call<SeriesResponse>

}