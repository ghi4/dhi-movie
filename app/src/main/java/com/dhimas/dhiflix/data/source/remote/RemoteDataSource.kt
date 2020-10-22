package com.dhimas.dhiflix.data.source.remote

import android.util.Log
import com.dhimas.dhiflix.data.source.remote.response.ShowListResponse
import com.dhimas.dhiflix.data.source.remote.response.ShowResponse
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RemoteDataSource private constructor(private val retrofitService: RetrofitInterface){

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(retrofitService: RetrofitInterface): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(retrofitService)
            }
    }

    fun getMovieList(callback: LoadMovieListCallback){
        val call = retrofitService.getMovieList()

        call.enqueue(object : Callback<ShowListResponse>{
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                if(response.isSuccessful){
                    val movieListResponse = response.body()?.showList
                    callback.onMovieListReceived(movieListResponse as ArrayList<ShowResponse>)
                }
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    fun getSeriesList(callback: LoadSeriesListCallback){
        val call = retrofitService.getSeriesList()

        call.enqueue(object : Callback<ShowListResponse>{
            override fun onResponse(
                call: Call<ShowListResponse>,
                response: Response<ShowListResponse>
            ) {
                val seriesListResponse = response.body()?.showList
                callback.onSeriesListReceived(seriesListResponse as ArrayList<ShowResponse>)
            }

            override fun onFailure(call: Call<ShowListResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    fun getMovieDetail(movie_id: String, callback: LoadMovieDetailCallback){
        val call = retrofitService.getMovieDetail(movie_id)

        call.enqueue(object : Callback<ShowResponse>{
            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                if(response.isSuccessful){
                    val movieResponse = response.body()
                    callback.onMovieDetailReceived(movieResponse as ShowResponse)
                }
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.d("Throwable", t.message.toString())
            }

        })
    }

    fun getSeriesDetail(series_id: String, callback: LoadSeriesDetailCallback){
        val call = retrofitService.getSeriesDetail(series_id)

        call.enqueue(object : Callback<ShowResponse>{
            override fun onResponse(call: Call<ShowResponse>, response: Response<ShowResponse>) {
                val seriesResponse = response.body()
                callback.onSeriesDetailReceived(seriesResponse as ShowResponse)
            }

            override fun onFailure(call: Call<ShowResponse>, t: Throwable) {
                Log.d("THrowable", t.message.toString())
            }

        })
    }

    interface LoadMovieListCallback {
        fun onMovieListReceived(movieListResponse: ArrayList<ShowResponse>)
    }

    interface LoadSeriesListCallback {
        fun onSeriesListReceived(seriesListResponse: ArrayList<ShowResponse>)
    }

    interface LoadMovieDetailCallback {
        fun onMovieDetailReceived(movieDetailResponse: ShowResponse)
    }

    interface LoadSeriesDetailCallback {
        fun onSeriesDetailReceived(seriesDetailResponse: ShowResponse)
    }
}