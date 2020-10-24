package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse

class ShowRepository private constructor(private val remoteDataSource: RemoteDataSource) :
        ShowDataSource {

    companion object {
        @Volatile
        private var instance: ShowRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): ShowRepository =
                instance ?: synchronized(this) {
                    instance ?: ShowRepository(remoteDataSource)
                }
    }

    override fun getMovieList(): LiveData<List<ShowEntity>> {
        val movieListResult = MutableLiveData<List<ShowEntity>>()

        remoteDataSource.getMovieList(object : RemoteDataSource.LoadMovieListCallback {
            override fun onMovieListReceived(movieListResponse: java.util.ArrayList<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()

                for (response in movieListResponse) {
                    val movie = ShowEntity(
                            response.movie_id,
                            response.title,
                            response.releaseDate,
                            response.overview,
                            response.posterPath,
                            response.backdropPath
                    )

                    movieList.add(movie)
                }

                movieListResult.postValue(movieList)
            }
        })

        return movieListResult
    }

    override fun getMovieDetail(movie_id: String): LiveData<ShowEntity> {
        val movieDetailResult = MutableLiveData<ShowEntity>()

        remoteDataSource.getMovieDetail(
                movie_id,
                object : RemoteDataSource.LoadMovieDetailCallback {
                    override fun onMovieDetailReceived(movieDetailResponse: MovieResponse) {
                        val movieDetail = ShowEntity(
                                movieDetailResponse.movie_id,
                                movieDetailResponse.title,
                                movieDetailResponse.releaseDate,
                                movieDetailResponse.overview,
                                movieDetailResponse.posterPath,
                                movieDetailResponse.backdropPath
                        )

                        movieDetailResult.postValue(movieDetail)
                    }
                })

        return movieDetailResult
    }

    override fun getSeriesList(): LiveData<List<ShowEntity>> {
        val seriesListResult = MutableLiveData<List<ShowEntity>>()

        remoteDataSource.getSeriesList(object : RemoteDataSource.LoadSeriesListCallback {
            override fun onSeriesListReceived(seriesListResponse: java.util.ArrayList<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()

                for (response in seriesListResponse) {
                    val series = ShowEntity(
                            response.series_id,
                            response.name,
                            response.releaseDate,
                            response.overview,
                            response.posterPath,
                            response.backdropPath
                    )

                    seriesList.add(series)
                }
                seriesListResult.postValue(seriesList)
            }
        })

        return seriesListResult
    }

    override fun getSeriesDetail(series_id: String): LiveData<ShowEntity> {
        val seriesDetailResult = MutableLiveData<ShowEntity>()

        remoteDataSource.getSeriesDetail(
                series_id,
                object : RemoteDataSource.LoadSeriesDetailCallback {
                    override fun onSeriesDetailReceived(seriesDetailResponse: SeriesResponse) {
                        val seriesEntity = ShowEntity(
                                seriesDetailResponse.series_id,
                                seriesDetailResponse.name,
                                seriesDetailResponse.releaseDate,
                                seriesDetailResponse.overview,
                                seriesDetailResponse.posterPath,
                                seriesDetailResponse.backdropPath
                        )
                        seriesDetailResult.postValue(seriesEntity)
                    }
                })

        return seriesDetailResult
    }

}