package com.dhimas.dhiflix.data.source.remote

import android.util.Log
import com.dhimas.dhiflix.data.source.remote.response.MovieListResponse
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesListResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitInterface
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class RemoteDataSource private constructor(private val retrofitService: RetrofitInterface) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(retrofitService: RetrofitInterface): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(retrofitService)
            }
    }

    fun getMovieList(callback: LoadMovieListCallback) {
        val call = retrofitService.getMovieList()

        EspressoIdlingResource.increment()
        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful) {
                    val movieListResponse = response.body()?.showList
                    callback.onMovieListReceived(movieListResponse as ArrayList<MovieResponse>)
                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    fun getSeriesList(callback: LoadSeriesListCallback) {
        val call = retrofitService.getSeriesList()

        EspressoIdlingResource.increment()
        call.enqueue(object : Callback<SeriesListResponse> {
            override fun onResponse(
                call: Call<SeriesListResponse>,
                response: Response<SeriesListResponse>
            ) {
                val seriesListResponse = response.body()?.seriesList
                callback.onSeriesListReceived(seriesListResponse as ArrayList<SeriesResponse>)
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesListResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    fun getMovieDetail(movie_id: String, callback: LoadMovieDetailCallback) {
        val call = retrofitService.getMovieDetail(movie_id)

        EspressoIdlingResource.increment()
        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()
                    callback.onMovieDetailReceived(movieResponse as MovieResponse)
                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }
        })
    }

    fun getSeriesDetail(series_id: String, callback: LoadSeriesDetailCallback) {
        val call = retrofitService.getSeriesDetail(series_id)

        EspressoIdlingResource.increment()
        call.enqueue(object : Callback<SeriesResponse> {
            override fun onResponse(
                call: Call<SeriesResponse>,
                response: Response<SeriesResponse>
            ) {
                val seriesResponse = response.body()
                callback.onSeriesDetailReceived(seriesResponse as SeriesResponse)
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    interface LoadMovieListCallback {
        fun onMovieListReceived(movieListResponse: ArrayList<MovieResponse>)
    }

    interface LoadSeriesListCallback {
        fun onSeriesListReceived(seriesListResponse: ArrayList<SeriesResponse>)
    }

    interface LoadMovieDetailCallback {
        fun onMovieDetailReceived(movieDetailResponse: MovieResponse)
    }

    interface LoadSeriesDetailCallback {
        fun onSeriesDetailReceived(seriesDetailResponse: SeriesResponse)
    }
}