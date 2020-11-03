package com.dhimas.dhiflix.data.source.remote

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhimas.dhiflix.data.source.remote.response.MovieListResponse
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesListResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.data.source.remote.retrofit.RetrofitInterface
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val retrofitService: RetrofitInterface) {

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(retrofitService: RetrofitInterface): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(retrofitService)
            }
    }

    fun getMovieList(): LiveData<ApiResponse<List<MovieResponse>>> {
        val call = retrofitService.getMovieList()
        val resultMovie = MutableLiveData<ApiResponse<List<MovieResponse>>>()
        var movieListResponse = ArrayList<MovieResponse>()

        EspressoIdlingResource.increment()

        call.enqueue(object : Callback<MovieListResponse> {
            override fun onResponse(
                call: Call<MovieListResponse>,
                response: Response<MovieListResponse>
            ) {
                if (response.isSuccessful) {
                    val mMovieListResponse = response.body()?.movieList

                    if (!mMovieListResponse.isNullOrEmpty()) {
                        movieListResponse = mMovieListResponse as ArrayList<MovieResponse>
                        resultMovie.value = ApiResponse.success(movieListResponse)
                    } else {
                        movieListResponse = mMovieListResponse as ArrayList<MovieResponse>
                        resultMovie.value = ApiResponse.empty(movieListResponse, "No movie found.")
                    }

                }
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(movieListResponse, "An error occured.")
            }
        })

        EspressoIdlingResource.decrement()

        return resultMovie
    }

    fun getSeriesList(): LiveData<ApiResponse<List<SeriesResponse>>> {
        val call = retrofitService.getSeriesList()
        val resultSeries = MutableLiveData<ApiResponse<List<SeriesResponse>>>()
        var seriesListResponse = ArrayList<SeriesResponse>()

        EspressoIdlingResource.increment()

        call.enqueue(object : Callback<SeriesListResponse> {
            override fun onResponse(
                call: Call<SeriesListResponse>,
                response: Response<SeriesListResponse>
            ) {
                if (response.isSuccessful) {
                    val mSeriesListResponse = response.body()?.seriesList

                    if (!mSeriesListResponse.isNullOrEmpty()) {
                        seriesListResponse = mSeriesListResponse as ArrayList<SeriesResponse>
                        resultSeries.value = ApiResponse.success(seriesListResponse)
                    } else {
                        seriesListResponse = mSeriesListResponse as ArrayList<SeriesResponse>
                        resultSeries.value =
                            ApiResponse.empty(seriesListResponse, "No series found.")
                    }

                }
            }

            override fun onFailure(call: Call<SeriesListResponse>, t: Throwable) {
                resultSeries.value = ApiResponse.error(seriesListResponse, "An error occured.")
            }
        })

        Handler(Looper.getMainLooper()).postDelayed({
            EspressoIdlingResource.decrement()
        }, 2000)

        return resultSeries
    }

    fun getMovieDetail(movie_id: String): LiveData<ApiResponse<MovieResponse>> {
        val call = retrofitService.getMovieDetail(movie_id)
        val resultMovie = MutableLiveData<ApiResponse<MovieResponse>>()

        EspressoIdlingResource.increment()

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful) {
                    val movieResponse = response.body()

                    if (movieResponse != null) {
                        resultMovie.value = ApiResponse.success(movieResponse)
                    } else {
                        resultMovie.value = ApiResponse.error(MovieResponse(), "No movie found")
                    }

                    EspressoIdlingResource.decrement()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(MovieResponse(), "An error occured.")
            }
        })

        return resultMovie
    }

    fun getSeriesDetail(series_id: String): LiveData<ApiResponse<SeriesResponse>> {
        val call = retrofitService.getSeriesDetail(series_id)
        val resultSeries = MutableLiveData<ApiResponse<SeriesResponse>>()

        EspressoIdlingResource.increment()
        call.enqueue(object : Callback<SeriesResponse> {
            override fun onResponse(
                call: Call<SeriesResponse>,
                response: Response<SeriesResponse>
            ) {
                if (response.isSuccessful) {
                    val seriesResponse = response.body()

                    if (seriesResponse != null) {
                        resultSeries.value = ApiResponse.success(seriesResponse)
                    } else {
                        resultSeries.value = ApiResponse.empty(SeriesResponse(), "No Series Found")
                    }
                }
            }

            override fun onFailure(call: Call<SeriesResponse>, t: Throwable) {
                resultSeries.value = ApiResponse.error(SeriesResponse(), "An error occured.")
            }

        })

        return resultSeries
    }
}