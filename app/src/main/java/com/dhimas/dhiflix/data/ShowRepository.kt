package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.response.ShowResponse

class ShowRepository private constructor(private val remoteDataSource: RemoteDataSource): ShowDataSource{

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

        remoteDataSource.getMovieList(object : RemoteDataSource.LoadMovieListCallback{
            override fun onMovieListReceived(movieListResponse: java.util.ArrayList<ShowResponse>) {
                val movieList = ArrayList<ShowEntity>()

                for(response in movieListResponse){
                    val movie = ShowEntity(
                        response.show_id,
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

        remoteDataSource.getMovieDetail(movie_id, object : RemoteDataSource.LoadMovieDetailCallback{
            val movieDetail = ShowEntity()
            override fun onMovieDetailReceived(movieDetailResponse: ShowResponse) {
                movieDetail.id = movieDetailResponse.show_id
                movieDetail.title = movieDetailResponse.title
                movieDetail.overview = movieDetailResponse.overview
                movieDetail.posterPath = movieDetailResponse.posterPath
                movieDetail.backdropPath = movieDetailResponse.backdropPath

                movieDetailResult.postValue(movieDetail)
            }
        })

        return movieDetailResult
    }

    override fun getSeriesList(): LiveData<List<ShowEntity>> {
        val seriesListResult = MutableLiveData<List<ShowEntity>>()

        remoteDataSource.getSeriesList(object : RemoteDataSource.LoadSeriesListCallback{
            override fun onSeriesListReceived(seriesListResponse: java.util.ArrayList<ShowResponse>) {
                val seriesList = ArrayList<ShowEntity>()

                for(response in seriesListResponse){
                    val series = ShowEntity(
                            response.show_id,
                            response.title,
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

        remoteDataSource.getSeriesDetail(series_id, object : RemoteDataSource.LoadSeriesDetailCallback{
            override fun onSeriesDetailReceived(seriesDetailResponse: ShowResponse) {
                val seriesEntity = ShowEntity(
                        seriesDetailResponse.show_id,
                        seriesDetailResponse.title,
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