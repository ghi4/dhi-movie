package com.dhimas.dhiflix.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource.Factory
import com.dhimas.dhiflix.data.source.local.LocalDataSource
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.utils.AppExecutors
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.utils.LiveDataTest
import com.dhimas.dhiflix.utils.PagedListUtil
import com.dhimas.dhiflix.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class ShowRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val local = mock(LocalDataSource::class.java)
    private val appExecutors = mock(AppExecutors::class.java)

    private val showRepository = FakeShowRepository(remote, local, appExecutors)

    private val movieResponses = DummyData.generateRemoteDummyMovies()
    private val movieId = movieResponses[0].movie_id
    private val movieDetailResponse = movieResponses[0]

    private val seriesResponses = DummyData.generateRemoteDummySeries()
    private val seriesId = seriesResponses[0].series_id
    private val seriesDetailResponse = seriesResponses[0]

    @Test
    fun getAllMovies() {
        val dummyMovie = MutableLiveData<List<ShowEntity>>()
        `when`(local.getAllMovie()).thenReturn(dummyMovie)
        dummyMovie.value = DummyData.generateDummyMovies()

        val movieEntities = LiveDataTest.getValue(showRepository.getMovieList())
        verify(local).getAllMovie()

        assertNotNull(movieEntities.data)
        assertEquals(movieResponses.size.toLong(), movieEntities.data?.size?.toLong())

        val movieEntity = movieEntities.data?.get(0)
        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.id)
        assertNotNull(movieEntity?.title)
        assertNotNull(movieEntity?.releaseDate)
        assertNotNull(movieEntity?.overview)
        assertNotNull(movieEntity?.posterPath)
        assertNotNull(movieEntity?.backdropPath)
    }

    @Test
    fun getAllSeries() {
        val dummySeries = MutableLiveData<List<ShowEntity>>()
        `when`(local.getAllSeries()).thenReturn(dummySeries)
        dummySeries.value = DummyData.generateDummySeries()

        val seriesEntities = LiveDataTest.getValue(showRepository.getSeriesList())
        verify(local).getAllSeries()

        assertNotNull(seriesEntities.data)
        assertEquals(seriesResponses.size.toLong(), seriesEntities.data?.size?.toLong())

        val seriesEntity = seriesEntities.data?.get(0)
        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.id)
        assertNotNull(seriesEntity?.title)
        assertNotNull(seriesEntity?.releaseDate)
        assertNotNull(seriesEntity?.overview)
        assertNotNull(seriesEntity?.posterPath)
        assertNotNull(seriesEntity?.backdropPath)
    }

    @Test
    fun getMovieDetail() {
        val dummyEntity = MutableLiveData<ShowEntity>()
        dummyEntity.value = DummyData.generateDummyMovies()[0]

        `when`(local.getShowById(movieId)).thenReturn(dummyEntity)

        val movieEntity = LiveDataTest.getValue(showRepository.getMovieDetail(movieId))
        verify(local).getShowById(movieId)

        assertNotNull(movieEntity.data)

        val movieDetail = movieEntity.data

        assertNotNull(movieDetail)
        assertNotNull(movieDetail?.id)
        assertNotNull(movieDetail?.title)
        assertNotNull(movieDetail?.releaseDate)
        assertNotNull(movieDetail?.overview)
        assertNotNull(movieDetail?.posterPath)
        assertNotNull(movieDetail?.backdropPath)
        assertEquals(movieDetailResponse.movie_id, movieDetail?.id)
        assertEquals(movieDetailResponse.title, movieDetail?.title)
        assertEquals(movieDetailResponse.releaseDate, movieDetail?.releaseDate)
        assertEquals(movieDetailResponse.overview, movieDetail?.overview)
        assertEquals(movieDetailResponse.posterPath, movieDetail?.posterPath)
        assertEquals(movieDetailResponse.backdropPath, movieDetail?.backdropPath)
    }

    @Test
    fun getSeriesDetail() {
        val dummySeries = MutableLiveData<ShowEntity>()
        dummySeries.value = DummyData.generateDummySeries()[0]

        `when`(local.getShowById(seriesId)).thenReturn(dummySeries)

        val seriesEntity = LiveDataTest.getValue(showRepository.getSeriesDetail(seriesId))
        verify(local).getShowById(seriesId)

        assertNotNull(seriesEntity)

        val seriesDetail = seriesEntity.data
        assertNotNull(seriesDetail)
        assertNotNull(seriesDetail?.id)
        assertNotNull(seriesDetail?.title)
        assertNotNull(seriesDetail?.releaseDate)
        assertNotNull(seriesDetail?.overview)
        assertNotNull(seriesDetail?.posterPath)
        assertNotNull(seriesDetail?.backdropPath)
        assertEquals(seriesDetailResponse.series_id, seriesDetail?.id)
        assertEquals(seriesDetailResponse.name, seriesDetail?.title)
        assertEquals(seriesDetailResponse.releaseDate, seriesDetail?.releaseDate)
        assertEquals(seriesDetailResponse.overview, seriesDetail?.overview)
        assertEquals(seriesDetailResponse.posterPath, seriesDetail?.posterPath)
        assertEquals(seriesDetailResponse.backdropPath, seriesDetail?.backdropPath)
    }

    @Test
    fun getAllFavoriteMovie() {
        val dataSourceFactory = mock(Factory::class.java) as Factory<Int, ShowEntity>
        `when`(local.getAllFavoriteMovie()).thenReturn(dataSourceFactory)
        showRepository.getFavoriteMovieList()

        val movieEntities = Resource.success(PagedListUtil.mockPagedList(DummyData.generateDummyMovies()))
        verify(local).getAllFavoriteMovie()

        assertNotNull(movieEntities.data)
        assertEquals(movieResponses.size.toLong(), movieEntities.data?.size?.toLong())

        val movieEntity = movieEntities.data?.get(0)
        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.id)
        assertNotNull(movieEntity?.title)
        assertNotNull(movieEntity?.releaseDate)
        assertNotNull(movieEntity?.overview)
        assertNotNull(movieEntity?.posterPath)
        assertNotNull(movieEntity?.backdropPath)
    }

    @Test
    fun getAllFavoriteSeries() {
        val dataSourceFactory = mock(Factory::class.java) as Factory<Int, ShowEntity>
        `when`(local.getAllFavoriteSeries()).thenReturn(dataSourceFactory)
        showRepository.getFavoriteSeriesList()

        val seriesEntities = Resource.success(PagedListUtil.mockPagedList(DummyData.generateDummySeries()))
        verify(local).getAllFavoriteSeries()

        assertNotNull(seriesEntities.data)
        assertEquals(seriesResponses.size, seriesEntities.data?.size)

        assertNotNull(seriesEntities.data)
        assertEquals(seriesResponses.size.toLong(), seriesEntities.data?.size?.toLong())

        val seriesEntity = seriesEntities.data?.get(0)
        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.id)
        assertNotNull(seriesEntity?.title)
        assertNotNull(seriesEntity?.releaseDate)
        assertNotNull(seriesEntity?.overview)
        assertNotNull(seriesEntity?.posterPath)
        assertNotNull(seriesEntity?.backdropPath)
    }

}