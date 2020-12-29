package com.dhimas.dhiflix.core.data

import com.dhimas.dhiflix.core.data.source.local.LocalDataSource
import com.dhimas.dhiflix.core.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.core.data.source.remote.ApiResponse
import com.dhimas.dhiflix.core.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.core.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.core.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.repository.IShowRepository
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ShowRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IShowRepository {

    override fun getMovieList(page: Int): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getMovies(page * 20).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovieList(page)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it)
                    movieList.add(movie)
                }

                localDataSource.insertShows(movieList)
            }
        }.asFlow()

    }

    override fun getSeriesList(page: Int): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSeries(page * 20).map {
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
                    val series = DataMapper.mapSeriesResponseToEntity(it)
                    seriesList.add(series)
                }

                localDataSource.insertShows(seriesList)
            }
        }.asFlow()

    }

    override fun getMovieDetail(movieId: String): Flow<Resource<Show>> {
        return object : NetworkBoundResource<Show, MovieResponse>() {
            public override fun loadFromDB(): Flow<Show> {
                return localDataSource.getShowById(movieId).map {
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override suspend fun createCall(): Flow<ApiResponse<MovieResponse>> {
                return remoteDataSource.getMovieDetail(movieId)
            }

            override suspend fun saveCallResult(data: MovieResponse) {
                val movie = DataMapper.mapMovieResponseToEntity(data)
                localDataSource.insertShows(listOf(movie))
            }
        }.asFlow()
    }

    override fun getSeriesDetail(seriesId: String): Flow<Resource<Show>> {
        return object : NetworkBoundResource<Show, SeriesResponse>() {
            public override fun loadFromDB(): Flow<Show> {
                return localDataSource.getShowById(seriesId).map {
                    DataMapper.mapEntityToDomain(it)
                }
            }

            override fun shouldFetch(data: Show?): Boolean {
                return data == null
            }

            override suspend fun createCall(): Flow<ApiResponse<SeriesResponse>> {
                return remoteDataSource.getSeriesDetail(seriesId)
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

    override fun getSimilarMovieList(movieId: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<MovieResponse>>() {
            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSimilarMovies(movieId).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getSimilarMovieList(movieId)
            }

            override suspend fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSimilar = 1

                data.map {
                    val movie = DataMapper.mapMovieResponseToEntity(it, isSimilar = isSimilar)
                    movieList.add(movie)
                }

                //Delete old similar list
                localDataSource.deleteSimilarExcept(movieId, Const.MOVIE_TYPE)

                //Insert new similar list
                localDataSource.insertShows(movieList)
            }
        }.asFlow()
    }

    override fun getSimilarSeriesList(seriesId: String): Flow<Resource<List<Show>>> {
        return object :
            NetworkBoundResource<List<Show>, List<SeriesResponse>>() {

            public override fun loadFromDB(): Flow<List<Show>> {
                return localDataSource.getSimilarSeries(seriesId).map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override fun shouldFetch(data: List<Show>?): Boolean = true

            override suspend fun createCall(): Flow<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSimilarSeriesList(seriesId)
            }

            override suspend fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSimilar = 1

                data.map {
                    val series = DataMapper.mapSeriesResponseToEntity(it, isSimilar = isSimilar)
                    seriesList.add(series)
                }

                //Delete old similar list
                localDataSource.deleteSimilarExcept(seriesId, Const.SERIES_TYPE)

                //Insert new similar list
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

                //Delete old search result
                localDataSource.deleteAllSearchShow(Const.MOVIE_TYPE)

                //Insert new search result
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

                //Delete old search result
                localDataSource.deleteAllSearchShow(Const.SERIES_TYPE)

                //Insert new search result
                localDataSource.insertShows(seriesList)
            }
        }.asFlow()
    }

    override suspend fun setFavorite(show: Show) {
        val showEntity = DataMapper.mapDomainToEntity(show)
        localDataSource.setFavorite(showEntity)

    }

}