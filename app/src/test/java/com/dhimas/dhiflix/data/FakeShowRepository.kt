package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.source.local.LocalDataSource
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.remote.ApiResponse
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.utils.AppExecutors
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.vo.Resource

class FakeShowRepository constructor(
        private val remoteDataSource: RemoteDataSource,
        private val localDataSource: LocalDataSource,
        private val appExecutors: AppExecutors)
    : ShowDataSource {

    override fun getMovieList(): LiveData<Resource<List<ShowEntity>>> {
        return object : NetworkBoundResource<List<ShowEntity>, List<MovieResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<List<ShowEntity>> =
                localDataSource.getAllMovie()

            override fun shouldFetch(data: List<ShowEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getMovieList()

            override fun saveCallResult(data: List<MovieResponse>) {
                val showList = ArrayList<ShowEntity>()
                for(response in data) {
                    val show = ShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.MOVIE_TYPE,
                        0
                    )

                    showList.add(show)
                }
                localDataSource.insertShows(showList)
            }

        }.asLiveData()
    }

    override fun getSeriesList(): LiveData<Resource<List<ShowEntity>>> {
        return object : NetworkBoundResource<List<ShowEntity>, List<SeriesResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<List<ShowEntity>> =
                localDataSource.getAllSeries()

            override fun shouldFetch(data: List<ShowEntity>?): Boolean =
                data == null || data.isEmpty()

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSeriesList()

            override fun saveCallResult(data: List<SeriesResponse>) {
                val showList = ArrayList<ShowEntity>()
                for(response in data) {
                    val show = ShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.SERIES_TYPE,
                        0
                    )

                    showList.add(show)
                }
                localDataSource.insertShows(showList)
            }

        }.asLiveData()
    }

    override fun getMovieDetail(movie_id: String): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, MovieResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<ShowEntity> =
                localDataSource.getShowById(movie_id)

            override fun shouldFetch(data: ShowEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> =
                remoteDataSource.getMovieDetail(movie_id)

            override fun saveCallResult(data: MovieResponse) {
                val movie = ShowEntity(
                    data.movie_id,
                    data.title,
                    data.releaseDate,
                    data.overview,
                    data.posterPath,
                    data.backdropPath,
                    Constant.MOVIE_TYPE
                )
                localDataSource.insertShows(listOf(movie))
            }
        }.asLiveData()
    }

    override fun getSeriesDetail(series_id: String): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, SeriesResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<ShowEntity> =
                localDataSource.getShowById(series_id)

            override fun shouldFetch(data: ShowEntity?): Boolean =
                data == null

            override fun createCall(): LiveData<ApiResponse<SeriesResponse>> =
                remoteDataSource.getSeriesDetail(series_id)

            override fun saveCallResult(data: SeriesResponse) {
                val series = ShowEntity(
                    data.series_id,
                    data.name,
                    data.releaseDate,
                    data.overview,
                    data.posterPath,
                    data.backdropPath,
                    Constant.MOVIE_TYPE
                )
                localDataSource.insertShows(listOf(series))
            }
        }.asLiveData()
    }

    override fun getFavoriteMovieList(): LiveData<Resource<PagedList<ShowEntity>>> {
        return object : NetworkBoundResource<PagedList<ShowEntity>, List<MovieResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()

                return LivePagedListBuilder(localDataSource.getAllFavoriteMovie(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getMovieList()

            override fun saveCallResult(data: List<MovieResponse>) {

            }

        }.asLiveData()
    }

    override fun getFavoriteSeriesList(): LiveData<Resource<PagedList<ShowEntity>>> {
        return object : NetworkBoundResource<PagedList<ShowEntity>, List<SeriesResponse>>(appExecutors){
            override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()

                return LivePagedListBuilder(localDataSource.getAllFavoriteSeries(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSeriesList()

            override fun saveCallResult(data: List<SeriesResponse>) {

            }

        }.asLiveData()
    }

//    override fun getMovieList(): LiveData<List<ShowEntity>> {
//        val movieListResult = MutableLiveData<List<ShowEntity>>()
//
//        remoteDataSource.getMovieList(object : RemoteDataSource.LoadMovieListCallback {
//            override fun onMovieListReceived(movieListResponse: java.util.ArrayList<MovieResponse>) {
//                val movieList = ArrayList<ShowEntity>()
//
//                for (response in movieListResponse) {
//                    val movie = ShowEntity(
//                        response.movie_id,
//                        response.title,
//                        response.releaseDate,
//                        response.overview,
//                        response.posterPath,
//                        response.backdropPath
//                    )
//
//                    movieList.add(movie)
//                }
//
//                movieListResult.postValue(movieList)
//            }
//        })
//
//        return movieListResult
//    }
//
//    override fun getMovieDetail(movie_id: String): LiveData<ShowEntity> {
//        val movieDetailResult = MutableLiveData<ShowEntity>()
//
//        remoteDataSource.getMovieDetail(
//            movie_id,
//            object : RemoteDataSource.LoadMovieDetailCallback {
//                override fun onMovieDetailReceived(movieDetailResponse: MovieResponse) {
//                    val movieDetail = ShowEntity(
//                        movieDetailResponse.movie_id,
//                        movieDetailResponse.title,
//                        movieDetailResponse.releaseDate,
//                        movieDetailResponse.overview,
//                        movieDetailResponse.posterPath,
//                        movieDetailResponse.backdropPath
//                    )
//
//                    movieDetailResult.postValue(movieDetail)
//                }
//            })
//
//        return movieDetailResult
//    }
//
//    override fun getSeriesList(): LiveData<List<ShowEntity>> {
//        val seriesListResult = MutableLiveData<List<ShowEntity>>()
//
//        remoteDataSource.getSeriesList(object : RemoteDataSource.LoadSeriesListCallback {
//            override fun onSeriesListReceived(seriesListResponse: java.util.ArrayList<SeriesResponse>) {
//                val seriesList = ArrayList<ShowEntity>()
//
//                for (response in seriesListResponse) {
//                    val series = ShowEntity(
//                        response.series_id,
//                        response.name,
//                        response.releaseDate,
//                        response.overview,
//                        response.posterPath,
//                        response.backdropPath
//                    )
//
//                    seriesList.add(series)
//                }
//                seriesListResult.postValue(seriesList)
//            }
//        })
//
//        return seriesListResult
//    }
//
//    override fun getSeriesDetail(series_id: String): LiveData<ShowEntity> {
//        val seriesDetailResult = MutableLiveData<ShowEntity>()
//
//        remoteDataSource.getSeriesDetail(
//            series_id,
//            object : RemoteDataSource.LoadSeriesDetailCallback {
//                override fun onSeriesDetailReceived(seriesDetailResponse: SeriesResponse) {
//                    val seriesEntity = ShowEntity(
//                        seriesDetailResponse.series_id,
//                        seriesDetailResponse.name,
//                        seriesDetailResponse.releaseDate,
//                        seriesDetailResponse.overview,
//                        seriesDetailResponse.posterPath,
//                        seriesDetailResponse.backdropPath
//                    )
//                    seriesDetailResult.postValue(seriesEntity)
//                }
//            })
//
//        return seriesDetailResult
//    }

}