package com.dhimas.dhiflix.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.dhimas.dhiflix.data.source.remote.RemoteDataSource
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.utils.LiveDataTest
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.mockito.Mockito.mock

internal class ShowRepositoryTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val remote = mock(RemoteDataSource::class.java)
    private val showRepository = FakeShowRepository(remote)

    private val movieResponses = DummyData.generateRemoteDummyMovies()
    private val movieId = movieResponses[0].show_id
    private val movieDetailResponse = movieResponses[0]

    private val seriesResponses = DummyData.generateRemoteDummySeries()
    private val seriesId = seriesResponses[0].series_id
    private val seriesDetailResponse = seriesResponses[0]

    @Test
    fun getAllMovies() {
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadMovieListCallback).onMovieListReceived(
                movieResponses
            )
            null
        }.`when`(remote).getMovieList(any())

        val movieEntities = LiveDataTest.getValue(showRepository.getMovieList())
        verify(remote).getMovieList(any())

        assertNotNull(movieEntities)
        assertEquals(movieResponses.size, movieEntities.size)
    }

    @Test
    fun getAllSeries() {
        doAnswer { invocation ->
            (invocation.arguments[0] as RemoteDataSource.LoadSeriesListCallback).onSeriesListReceived(
                seriesResponses
            )
            null
        }.`when`(remote).getSeriesList(any())

        val seriesEntities = LiveDataTest.getValue(showRepository.getSeriesList())
        verify(remote).getSeriesList(any())

        assertNotNull(seriesEntities)
        assertEquals(seriesResponses.size, seriesEntities.size)
    }

    @Test
    fun getMovieDetail() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadMovieDetailCallback).onMovieDetailReceived(
                movieDetailResponse
            )
            null
        }.`when`(remote).getMovieDetail(eq(movieId), any())

        val movieDetail = LiveDataTest.getValue(showRepository.getMovieDetail(movieId))
        verify(remote).getMovieDetail(eq(movieId), any())

        assertNotNull(movieDetail)
        assertEquals(movieDetailResponse.title, movieDetail.title)
    }

    @Test
    fun getSeriesDetail() {
        doAnswer { invocation ->
            (invocation.arguments[1] as RemoteDataSource.LoadSeriesDetailCallback).onSeriesDetailReceived(
                seriesDetailResponse
            )
            null
        }.`when`(remote).getSeriesDetail(eq(seriesId), any())

        val seriesDetail = LiveDataTest.getValue(showRepository.getSeriesDetail(seriesId))
        verify(remote).getSeriesDetail(eq(seriesId), any())

        assertNotNull(seriesDetail)
        assertEquals(seriesDetailResponse.name, seriesDetail.title)
    }

}