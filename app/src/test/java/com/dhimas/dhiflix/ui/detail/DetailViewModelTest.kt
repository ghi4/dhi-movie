package com.dhimas.dhiflix.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<ShowEntity>

    @Before
    fun setUp() {
        viewModel = DetailViewModel(showRepository)
    }

    @Test
    fun getMovieDetail() {
        val dummyMovie = DummyData.generateDummyMovies()[0]
        val movie = MutableLiveData<ShowEntity>()
        movie.value = dummyMovie

        `when`(showRepository.getMovieDetail(dummyMovie.id!!)).thenReturn(movie)

        val movieEntity =
            viewModel.getShowEntityById(dummyMovie.id!!, DetailActivity.EXTRA_FROM_MOVIES).value
        verify(showRepository).getMovieDetail(dummyMovie.id!!)

        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.id)
        assertNotNull(movieEntity?.title)
        assertNotNull(movieEntity?.releaseDate)
        assertNotNull(movieEntity?.overview)
        assertNotNull(movieEntity?.posterPath)
        assertNotNull(movieEntity?.backdropPath)

        assertEquals(dummyMovie, movieEntity)
        assertEquals(dummyMovie.id, movieEntity?.id)
        assertEquals(dummyMovie.title, movieEntity?.title)
        assertEquals(dummyMovie.releaseDate, movieEntity?.releaseDate)
        assertEquals(dummyMovie.overview, movieEntity?.overview)
        assertEquals(dummyMovie.posterPath, movieEntity?.posterPath)
        assertEquals(dummyMovie.backdropPath, movieEntity?.backdropPath)

        viewModel.getShowEntityById(dummyMovie.id!!, DetailActivity.EXTRA_FROM_MOVIES)
            .observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }

    @Test
    fun getSeriesDetail() {
        val dummySeries = DummyData.generateDummySeries()[0]
        val series = MutableLiveData<ShowEntity>()
        series.value = dummySeries

        `when`(showRepository.getSeriesDetail(dummySeries.id!!)).thenReturn(series)

        val seriesEntity =
            viewModel.getShowEntityById(dummySeries.id!!, DetailActivity.EXTRA_FROM_SERIES).value
        verify(showRepository).getSeriesDetail(dummySeries.id!!)

        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.id)
        assertNotNull(seriesEntity?.title)
        assertNotNull(seriesEntity?.releaseDate)
        assertNotNull(seriesEntity?.overview)
        assertNotNull(seriesEntity?.posterPath)
        assertNotNull(seriesEntity?.backdropPath)

        assertEquals(dummySeries, seriesEntity)
        assertEquals(dummySeries.id, seriesEntity?.id)
        assertEquals(dummySeries.title, seriesEntity?.title)
        assertEquals(dummySeries.releaseDate, seriesEntity?.releaseDate)
        assertEquals(dummySeries.overview, seriesEntity?.overview)
        assertEquals(dummySeries.posterPath, seriesEntity?.posterPath)
        assertEquals(dummySeries.backdropPath, seriesEntity?.backdropPath)

        viewModel.getShowEntityById(dummySeries.id!!, DetailActivity.EXTRA_FROM_SERIES)
            .observeForever(observer)
        verify(observer).onChanged(dummySeries)
    }
}