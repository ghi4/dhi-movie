package com.dhimas.dhiflix.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.dhimas.dhiflix.core.data.source.local.LocalDataSource
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.remote.ApiResponse
import com.dhimas.dhiflix.core.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.repository.ShowDataSource
import com.dhimas.dhiflix.core.utils.AppExecutors
import com.dhimas.dhiflix.core.utils.DataMapper

class ShowRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : ShowDataSource {

    companion object {
        @Volatile
        private var instance: ShowRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): ShowRepository =
            instance ?: synchronized(this) {
                instance ?: ShowRepository(remoteDataSource, localData, appExecutors)
            }
    }

    override fun getMovieList(page: Int): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getMovies(page).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovieList(page)
            }

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it, page = page)
                    movieList.add(movie)
                }

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()

    }

    override fun getSeriesList(page: Int): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getSeries(page).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSeriesList(page)
            }

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, page = page)
                    seriesList.add(series)
                }

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()

    }

    override fun getMovieDetail(movie_id: String): LiveData<Resource<Show>> {
        return object : NetworkBoundResource<Show, MovieResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<Show> {
                return Transformations.map(localDataSource.getShowById(movie_id)){
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> {
                return remoteDataSource.getMovieDetail(movie_id)
            }

            override fun saveCallResult(data: MovieResponse) {
                val movie = DataMapper.mapMovieResponseToEntity(data)
                localDataSource.insertShows(listOf(movie))
            }
        }.asLiveData()
    }

    override fun getSeriesDetail(series_id: String): LiveData<Resource<Show>> {
        return object : NetworkBoundResource<Show, SeriesResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<Show> {
                return Transformations.map(localDataSource.getShowById(series_id)){
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<SeriesResponse>> {
                return remoteDataSource.getSeriesDetail(series_id)
            }

            override fun saveCallResult(data: SeriesResponse) {
                val series = DataMapper.mapSeriesResponseToEntity(data)
                localDataSource.insertShows(listOf(series))
            }
        }.asLiveData()
    }

    override fun getFavoriteMovieList(): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getFavoriteMovies().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovieList(1)
            }

            override fun saveCallResult(data: List<MovieResponse>) {

            }
        }.asLiveData()
    }

    override fun getFavoriteSeriesList(): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getFavoriteSeries().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSeriesList(1)
            }

            override fun saveCallResult(data: List<SeriesResponse>) {

            }

        }.asLiveData()
    }

    override fun getSimilarMovieList(movie_id: String): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getSimilarMovies().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getSimilarMovieList(movie_id)
            }

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSimilar = 1

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it, isSimilar = isSimilar)
                    movieList.add(movie)
                }

//                if (!movieList.isNullOrEmpty())
//                    localDataSource.deleteAllSimilarShow(Const.MOVIE_TYPE)

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()
    }

    override fun getSimilarSeriesList(series_id: String): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>(appExecutors) {

            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.getSimilarSeries().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSimilarSeriesList(series_id)
            }

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSimilar = 1

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, isSimilar = isSimilar)
                    seriesList.add(series)
                }

//                if (!seriesList.isNullOrEmpty())
//                    localDataSource.deleteAllSimilarShow(Const.SERIES_TYPE)

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()
    }

    override fun searchMovie(keyword: String): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.searchMovies("$keyword%").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.searchMovie(keyword)
            }

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSearch = 1

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it, isSearch = isSearch)
                    movieList.add(movie)
                }

//                localDataSource.deleteAllSearchShow(Const.MOVIE_TYPE)

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()
    }

    override fun searchSeries(keyword: String): LiveData<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<Show>> {
                return localDataSource.searchSeries("$keyword%").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.searchSeries(keyword)
            }

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSearch = 1

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, isSearch = isSearch)
                    seriesList.add(series)
                }

//                localDataSource.deleteAllSearchShow(Const.SERIES_TYPE)

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()
    }

    override fun setFavorite(show: Show) {
        val showEntity = DataMapper.mapDomainToEntity(show)
        appExecutors.diskIO().execute { localDataSource.setFavorite(showEntity) }
    }

}