package com.dhimas.dhiflix.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.vo.Resource
import com.nhaarman.mockitokotlin2.verify
import junit.framework.Assert.assertNotNull
import org.junit.Assert.assertEquals
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
    private lateinit var showId: String

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<ShowEntity>>

    @Before
    fun setUp() {
        viewModel = DetailViewModel(showRepository)
        showId = "123"
    }

    @Test
    fun getMovieDetail() {
        val dummyMovieResource = Resource.success(DummyData.generateDummyMovies()[0])
        val movie = MutableLiveData<Resource<ShowEntity>>()
        movie.value = dummyMovieResource
        val dummyMovie = dummyMovieResource.data as ShowEntity

        viewModel.setDoubleTrigger(showId, Const.MOVIE_TYPE)
        `when`(showRepository.getMovieDetail(showId)).thenReturn(movie)
        viewModel.getShowEntityById().observeForever(observer)
        verify(observer).onChanged(dummyMovieResource)

        val movieEntity = viewModel.getShowEntityById().value?.data
        verify(showRepository).getMovieDetail(showId)

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
    }

    @Test
    fun getSeriesDetail() {
        val dummySeriesResource = Resource.success(DummyData.generateDummySeries()[0])
        val series = MutableLiveData<Resource<ShowEntity>>()
        series.value = dummySeriesResource
        val dummySeries = dummySeriesResource.data as ShowEntity

        viewModel.setDoubleTrigger(showId, Const.SERIES_TYPE)
        `when`(showRepository.getSeriesDetail(showId)).thenReturn(series)
        viewModel.getShowEntityById().observeForever(observer)
        verify(observer).onChanged(dummySeriesResource)


        val seriesEntity = viewModel.getShowEntityById().value?.data
        verify(showRepository).getSeriesDetail(showId)

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
    }
}