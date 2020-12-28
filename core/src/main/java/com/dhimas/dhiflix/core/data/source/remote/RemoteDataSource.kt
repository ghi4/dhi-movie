package com.dhimas.dhiflix.core.data.source.remote

import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.core.data.source.remote.retrofit.RetrofitInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val retrofitService: RetrofitInterface) {
    private val noInternet = "Internet connection issue."

    suspend fun getMovieList(page: Int): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = retrofitService.getMovieList(page = page)
                val data = response.movieList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSeriesList(page: Int): Flow<ApiResponse<List<SeriesResponse>>> {
        return flow {
            try {
                val response = retrofitService.getSeriesList(page = page)
                val data = response.seriesList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getMovieDetail(movieId: String): Flow<ApiResponse<MovieResponse>> {
        return flow {
            try {
                val response = retrofitService.getMovieDetail(movieId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSeriesDetail(seriesId: String): Flow<ApiResponse<SeriesResponse>> {
        return flow {
            try {
                val response = retrofitService.getSeriesDetail(seriesId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSimilarMovieList(movieId: String): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = retrofitService.getSimilarMovie(movieId = movieId)
                val data = response.movieList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getSimilarSeriesList(seriesId: String): Flow<ApiResponse<List<SeriesResponse>>> {
        return flow {
            try {
                val response = retrofitService.getSimilarSeries(seriesId)
                val data = response.seriesList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchMovie(keyword: String): Flow<ApiResponse<List<MovieResponse>>> {
        return flow {
            try {
                val response = retrofitService.searchMovie(keyword = keyword)
                val data = response.movieList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun searchSeries(keyword: String): Flow<ApiResponse<List<SeriesResponse>>> {
        return flow {
            try {
                val response = retrofitService.searchSeries(keyword = keyword)
                val data = response.seriesList

                if (data.isNotEmpty()) {
                    emit(ApiResponse.Success(data))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(noInternet))
            }
        }.flowOn(Dispatchers.IO)
    }
}