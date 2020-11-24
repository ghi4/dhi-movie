package com.dhimas.dhiflix.data.source.remote

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
    val noInternet = "Internet connection issue."
    val noMovies = "No movies found."
    val noSeries = "No series found."

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(retrofitService: RetrofitInterface): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(retrofitService)
            }
    }

    fun getMovieList(page: Int): LiveData<ApiResponse<List<MovieResponse>>> {
        val call = retrofitService.getMovieList(page)
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
                        resultMovie.value = ApiResponse.empty(movieListResponse, noMovies)
                    }

                    EspressoIdlingResource.decrement()
                }

            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(movieListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

        return resultMovie
    }

    fun getSeriesList(page: Int): LiveData<ApiResponse<List<SeriesResponse>>> {
        val call = retrofitService.getSeriesList(page)
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
                            ApiResponse.error(seriesListResponse, noSeries)
                    }
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesListResponse>, t: Throwable) {
                resultSeries.value =
                    ApiResponse.error(seriesListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

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
                        resultMovie.value = ApiResponse.error(MovieResponse(), noMovies)
                    }
                }

                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(MovieResponse(), noInternet)
                EspressoIdlingResource.decrement()
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
                        resultSeries.value = ApiResponse.empty(SeriesResponse(), noSeries)
                    }
                }

                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesResponse>, t: Throwable) {
                resultSeries.value = ApiResponse.error(SeriesResponse(), noInternet)
                EspressoIdlingResource.decrement()
            }

        })

        return resultSeries
    }

    fun getSimilarMovieList(movie_id: String): LiveData<ApiResponse<List<MovieResponse>>> {
        val call = retrofitService.getSimilarMovie(movie_id)
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
                        resultMovie.value = ApiResponse.empty(movieListResponse, noMovies)
                    }
                }


                EspressoIdlingResource.decrement()

            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(movieListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

        return resultMovie
    }

    fun getSimilarSeriesList(series_id: String): LiveData<ApiResponse<List<SeriesResponse>>> {
        val call = retrofitService.getSimilarSeries(series_id)
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
                            ApiResponse.empty(seriesListResponse, noSeries)
                    }
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesListResponse>, t: Throwable) {
                resultSeries.value =
                    ApiResponse.error(seriesListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

        return resultSeries
    }

    fun searchMovie(keyword: String): LiveData<ApiResponse<List<MovieResponse>>> {
        val call = retrofitService.searchMovie(keyword)
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
                        resultMovie.value = ApiResponse.empty(movieListResponse, noMovies)
                    }
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                resultMovie.value = ApiResponse.error(movieListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

        return resultMovie
    }

    fun searchSeries(keyword: String): LiveData<ApiResponse<List<SeriesResponse>>> {
        val call = retrofitService.searchSeries(keyword)
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
                            ApiResponse.empty(seriesListResponse, noSeries)
                    }
                }
                EspressoIdlingResource.decrement()
            }

            override fun onFailure(call: Call<SeriesListResponse>, t: Throwable) {
                resultSeries.value =
                    ApiResponse.error(seriesListResponse, noInternet)
                EspressoIdlingResource.decrement()
            }
        })

        return resultSeries
    }
}
