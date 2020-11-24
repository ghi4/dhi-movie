package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.source.local.LocalDataSource
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.remote.ApiResponse
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.data.source.remote.response.MovieResponse
import com.dhimas.dhiflix.data.source.remote.response.SeriesResponse
import com.dhimas.dhiflix.utils.AppExecutors
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.vo.Resource

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

    fun pagedListConfigBuilder(): PagedList.Config {
        return PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(9)
            .setPageSize(9)
            .build()
    }

    override fun getMovieList(page: Int): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.getMovies(page)
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> {
                return remoteDataSource.getMovieList(page)
            }

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()

                for (response in data) {
                    val movie = ShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.MOVIE_TYPE,
                        page
                    )

                    movieList.add(movie)
                }

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()

    }

    override fun getSeriesList(page: Int): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.getSeries(page)
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean {
                return data == null || data.isEmpty() || data.size != page * 20
            }

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> {
                return remoteDataSource.getSeriesList(page)
            }

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()

                for (response in data) {
                    val series = ShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.SERIES_TYPE,
                        page
                    )

                    seriesList.add(series)
                }

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()

    }

    override fun getMovieDetail(movie_id: String): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, MovieResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<ShowEntity> {
                return localDataSource.getShowById(movie_id)
            }

            override fun shouldFetch(data: ShowEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<MovieResponse>> {
                return remoteDataSource.getMovieDetail(movie_id)
            }

            override fun saveCallResult(data: MovieResponse) {
                val movie = ShowEntity(
                    data.movie_id,
                    data.title,
                    data.releaseDate,
                    data.overview,
                    data.posterPath,
                    data.backdropPath,
                    Const.MOVIE_TYPE
                )
                localDataSource.insertShows(listOf(movie))
            }
        }.asLiveData()
    }

    override fun getSeriesDetail(series_id: String): LiveData<Resource<ShowEntity>> {
        return object : NetworkBoundResource<ShowEntity, SeriesResponse>(appExecutors) {
            public override fun loadFromDB(): LiveData<ShowEntity> {
                return localDataSource.getShowById(series_id)
            }

            override fun shouldFetch(data: ShowEntity?): Boolean {
                return data == null
            }

            override fun createCall(): LiveData<ApiResponse<SeriesResponse>> {
                return remoteDataSource.getSeriesDetail(series_id)
            }

            override fun saveCallResult(data: SeriesResponse) {
                val series = ShowEntity(
                    data.series_id,
                    data.name,
                    data.releaseDate,
                    data.overview,
                    data.posterPath,
                    data.backdropPath,
                    Const.MOVIE_TYPE
                )
                localDataSource.insertShows(listOf(series))
            }
        }.asLiveData()
    }

    override fun getFavoriteMovieList(): LiveData<Resource<PagedList<ShowEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ShowEntity>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(4)
                    .setPageSize(4)
                    .build()

                return LivePagedListBuilder(localDataSource.getFavoriteMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getMovieList(1)

            override fun saveCallResult(data: List<MovieResponse>) {

            }
        }.asLiveData()
    }

    override fun getFavoriteSeriesList(): LiveData<Resource<PagedList<ShowEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ShowEntity>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = pagedListConfigBuilder()

                return LivePagedListBuilder(localDataSource.getFavoriteSeries(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = false

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSeriesList(1)

            override fun saveCallResult(data: List<SeriesResponse>) {

            }

        }.asLiveData()
    }

    override fun getSimilarMovieList(movie_id: String): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.getSimilarMovies()
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getSimilarMovieList(movie_id)

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSimilar = 1

                for (response in data) {
                    val movie = ShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.MOVIE_TYPE,
                        isSimilar = isSimilar
                    )

                    movieList.add(movie)
                }

                if (!movieList.isNullOrEmpty())
                    localDataSource.deleteAllSimilarShow(Const.MOVIE_TYPE)

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()
    }

    override fun getSimilarSeriesList(series_id: String): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<SeriesResponse>>(appExecutors) {

            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.getSimilarSeries()
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSimilarSeriesList(series_id)

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSimilar = 1

                for (response in data) {
                    val series = ShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.SERIES_TYPE,
                        isSimilar = isSimilar
                    )

                    seriesList.add(series)
                }

                if (!seriesList.isNullOrEmpty())
                    localDataSource.deleteAllSimilarShow(Const.SERIES_TYPE)

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()
    }

    override fun searchMovie(keyword: String): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.searchMovies("$keyword%")
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.searchMovie(keyword)

            override fun saveCallResult(data: List<MovieResponse>) {
                val movieList = ArrayList<ShowEntity>()
                val isSearch = 1

                for (response in data) {
                    val movie = ShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.MOVIE_TYPE,
                        isSearch = isSearch
                    )

                    movieList.add(movie)
                }

                localDataSource.deleteAllSearchShow(Const.MOVIE_TYPE)

                localDataSource.insertShows(movieList)
            }
        }.asLiveData()
    }

    override fun searchSeries(keyword: String): LiveData<Resource<List<ShowEntity>>> {
        return object :
            NetworkBoundResource<List<ShowEntity>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<List<ShowEntity>> {
                return localDataSource.searchSeries("$keyword%")
            }

            override fun shouldFetch(data: List<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.searchSeries(keyword)

            override fun saveCallResult(data: List<SeriesResponse>) {
                val seriesList = ArrayList<ShowEntity>()
                val isSearch = 1

                for (response in data) {
                    val series = ShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Const.SERIES_TYPE,
                        isSearch = isSearch
                    )

                    seriesList.add(series)
                }

                localDataSource.deleteAllSearchShow(Const.SERIES_TYPE)

                localDataSource.insertShows(seriesList)
            }
        }.asLiveData()
    }

    fun setFavorite(showEntity: ShowEntity) {
        appExecutors.diskIO().execute {
            localDataSource.setFavorite(showEntity)
        }
    }

}