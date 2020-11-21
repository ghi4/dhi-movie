package com.dhimas.dhiflix.data

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.source.local.LocalDataSource
import com.dhimas.dhiflix.data.source.local.entity.SearchShowEntity
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.local.entity.SimilarShowEntity
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
    private val appExecutors: AppExecutors
) : ShowDataSource {

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
                        Constant.MOVIE_TYPE,
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

            override fun shouldFetch(data: List<ShowEntity>?): Boolean =
                data == null || data.isEmpty() || data.size != page * 20

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSeriesList(page)

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
                        Constant.SERIES_TYPE,
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

    override fun getSimilarMovieList(movie_id: String): LiveData<Resource<PagedList<ShowEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ShowEntity>, List<MovieResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = pagedListConfigBuilder()

                return LivePagedListBuilder(localDataSource.getSimilarMovies(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<MovieResponse>>> =
                remoteDataSource.getSimilarMovieList(movie_id)

            override fun saveCallResult(data: List<MovieResponse>) {
                localDataSource.deleteAllSimilarShow()

                val movieList = ArrayList<SimilarShowEntity>()

                for (response in data) {
                    val movie = SimilarShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.MOVIE_TYPE
                    )

                    movieList.add(movie)
                }

                localDataSource.insertSimilarShows(movieList)
            }
        }.asLiveData()
    }

    override fun getSimilarSeriesList(series_id: String): LiveData<Resource<PagedList<ShowEntity>>> {
        return object :
            NetworkBoundResource<PagedList<ShowEntity>, List<SeriesResponse>>(appExecutors) {
            public override fun loadFromDB(): LiveData<PagedList<ShowEntity>> {
                val config = pagedListConfigBuilder()

                return LivePagedListBuilder(localDataSource.getSimilarSeries(), config).build()
            }

            override fun shouldFetch(data: PagedList<ShowEntity>?): Boolean = true

            override fun createCall(): LiveData<ApiResponse<List<SeriesResponse>>> =
                remoteDataSource.getSimilarSeriesList(series_id)

            override fun saveCallResult(data: List<SeriesResponse>) {
                localDataSource.deleteAllSimilarShow()

                val seriesList = ArrayList<SimilarShowEntity>()

                for (response in data) {
                    val series = SimilarShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.SERIES_TYPE
                    )

                    seriesList.add(series)
                }

                localDataSource.insertSimilarShows(seriesList)
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
                val movieList = ArrayList<SearchShowEntity>()

                localDataSource.deleteAllSearchShow()

                for (response in data) {
                    val movie = SearchShowEntity(
                        response.movie_id,
                        response.title,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.MOVIE_TYPE
                    )

                    movieList.add(movie)
                }

                localDataSource.insertSearchShows(movieList)
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
                val seriesList = ArrayList<SearchShowEntity>()

                localDataSource.deleteAllSearchShow()

                for (response in data) {
                    val series = SearchShowEntity(
                        response.series_id,
                        response.name,
                        response.releaseDate,
                        response.overview,
                        response.posterPath,
                        response.backdropPath,
                        Constant.SERIES_TYPE
                    )

                    seriesList.add(series)
                }

                localDataSource.insertSearchShows(seriesList)
            }
        }.asLiveData()
    }

}