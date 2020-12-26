package com.dhimas.dhiflix.core.data

import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShowRepository(
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

    override fun getMovieList(page: Int): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                Log.d("JJK", "Repo: LoadDB")
                return localDataSource.getMovies(page).map {
                    Log.d("JQA", "Repo: LoadDB - Mapper")
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean {
                Log.d("JJK", "Repo: ShouldFetch - ${data?.isEmpty()} - ${data?.size}")
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                Log.d("JJK", "Repo: Call")
                return remoteDataSource.getMovieList(page)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                Log.d("JJK", "Repo: SaveResult")
                val movieList = ArrayList<ShowEntity>()

                data.map {
                    Log.d("JJK", "Repo: SaveResult - Mapper")
                    val movie = DataMapper.mapMovieResponseToEntity(it, page = page)
                    movieList.add(movie)
                }

                Log.d("JJK", "Repo: SaveResult - Mapper - ${movieList.size}")
                localDataSource.insertShows(movieList)
            }
        }.asFlow()

    }

    override fun getSeriesList(page: Int): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSeries(page).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override suspend fun createCall(): Flow<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSeriesList(page)
            }

            override suspend fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, page = page)
                    seriesList.add(series)
                }

                localDataSource.insertShows(seriesList)
            }
        }.asFlow()

    }

    override fun getMovieDetail(movie_id: String): Flow<Resource<Show>> {
        return object : NetworkBoundResource<Show, MovieResponse>() {
            public override fun loadFromDB(): Flow<Show> {
                return localDataSource.getShowById(movie_id).map {
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override suspend fun createCall(): Flow<ApiResponse<MovieResponse>> {
                return remoteDataSource.getMovieDetail(movie_id)
            }

            override suspend fun saveCallResult(data: MovieResponse) {
                val movie = DataMapper.mapMovieResponseToEntity(data)
                localDataSource.insertShows(listOf(movie))
            }
        }.asFlow()
    }

    override fun getSeriesDetail(series_id: String): Flow<Resource<Show>> {
        return object : NetworkBoundResource<Show, SeriesResponse>() {
            public override fun loadFromDB(): Flow<Show> {
                return localDataSource.getShowById(series_id).map {
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override suspend fun createCall(): Flow<ApiResponse<SeriesResponse>> {
                return remoteDataSource.getSeriesDetail(series_id)
            }

            override suspend fun saveCallResult(data: SeriesResponse) {
                val series = DataMapper.mapSeriesResponseToEntity(data)
                localDataSource.insertShows(listOf(series))
            }
        }.asFlow()
    }

    override fun getFavoriteMovieList(): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getFavoriteMovies().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = false

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovieList(1)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {

            }
        }.asFlow()
    }

    override fun getFavoriteSeriesList(): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getFavoriteSeries().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = false

            override suspend fun createCall(): Flow<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSeriesList(1)
            }

            override suspend fun saveCallResult(data: List<SeriesResponse>) {

            }

        }.asFlow()
    }

    override fun getSimilarMovieList(movie_id: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSimilarMovies().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getSimilarMovieList(movie_id)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
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
        }.asFlow()
    }

    override fun getSimilarSeriesList(series_id: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {

            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSimilarSeries().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSimilarSeriesList(series_id)
            }

            override suspend fun saveCallResult(data: List<SeriesResponse>) {
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
        }.asFlow()
    }

    override fun searchMovie(keyword: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.searchMovies("$keyword%").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.searchMovie(keyword)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSearch = 1

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it, isSearch = isSearch)
                    movieList.add(movie)
                }

//                localDataSource.deleteAllSearchShow(Const.MOVIE_TYPE)

                localDataSource.insertShows(movieList)
            }
        }.asFlow()
    }

    override fun searchSeries(keyword: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.searchSeries("$keyword%").map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.searchSeries(keyword)
            }

            override suspend fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSearch = 1

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, isSearch = isSearch)
                    seriesList.add(series)
                }

//                localDataSource.deleteAllSearchShow(Const.SERIES_TYPE)

                localDataSource.insertShows(seriesList)
            }
        }.asFlow()
    }

    override fun setFavorite(show: Show) {
        val showEntity = DataMapper.mapDomainToEntity(show)
        appExecutors.diskIO().execute { localDataSource.setFavorite(showEntity) }
    }

}