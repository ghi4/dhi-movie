package com.dhimas.dhiflix.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {
    private lateinit var viewModel: SearchViewModel
    private lateinit var keyword: String

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<List<ShowEntity>>>

    @Before
    fun setUp() {
        keyword = "Hard"
        viewModel = SearchViewModel(showRepository)
        viewModel.setSearchQuery(keyword)
    }

    @Test
    fun getMovies() {
        val dummyMovieList =
            Resource.success(DummyData.generateDummyMovies())
        val movie = MutableLiveData<Resource<List<ShowEntity>>>()
        movie.value = dummyMovieList

        viewModel.setSearchQuery(keyword)
        `when`(showRepository.searchMovie(keyword)).thenReturn(movie)
        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovieList)

        val movieEntities = viewModel.getMovies().value?.data
        verify(showRepository).searchMovie(keyword)

        val dummyMovie = dummyMovieList.data?.get(0)
        val movieEntity = movieEntities?.get(0)

        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.id)
        assertNotNull(movieEntity?.title)
        assertNotNull(movieEntity?.releaseDate)
        assertNotNull(movieEntity?.overview)
        assertNotNull(movieEntity?.posterPath)
        assertNotNull(movieEntity?.backdropPath)

        assertEquals(dummyMovie, movieEntity)
        assertEquals(dummyMovie?.id, movieEntity?.id)
        assertEquals(dummyMovie?.title, movieEntity?.title)
        assertEquals(dummyMovie?.releaseDate, movieEntity?.releaseDate)
        assertEquals(dummyMovie?.overview, movieEntity?.overview)
        assertEquals(dummyMovie?.posterPath, movieEntity?.posterPath)
        assertEquals(dummyMovie?.backdropPath, movieEntity?.backdropPath)
        assertEquals(dummyMovieList.data?.size, movieEntities?.size)
    }

    @Test
    fun getSeries() {
        val dummySeriesList =
            Resource.success(DummyData.generateDummySeries())
        val series = MutableLiveData<Resource<List<ShowEntity>>>()
        series.value = dummySeriesList

        viewModel.setSearchQuery(keyword)
        `when`(showRepository.searchSeries(keyword)).thenReturn(series)
        viewModel.getSeries().observeForever(observer)
        verify(observer).onChanged(dummySeriesList)

        val seriesEntities = viewModel.getSeries().value?.data
        verify(showRepository).searchSeries(keyword)

        val dummySeries = dummySeriesList.data?.get(0)
        val seriesEntity = seriesEntities?.get(0)

        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.id)
        assertNotNull(seriesEntity?.title)
        assertNotNull(seriesEntity?.releaseDate)
        assertNotNull(seriesEntity?.overview)
        assertNotNull(seriesEntity?.posterPath)
        assertNotNull(seriesEntity?.backdropPath)

        assertEquals(dummySeries, seriesEntity)
        assertEquals(dummySeries?.id, seriesEntity?.id)
        assertEquals(dummySeries?.title, seriesEntity?.title)
        assertEquals(dummySeries?.releaseDate, seriesEntity?.releaseDate)
        assertEquals(dummySeries?.overview, seriesEntity?.overview)
        assertEquals(dummySeries?.posterPath, seriesEntity?.posterPath)
        assertEquals(dummySeries?.backdropPath, seriesEntity?.backdropPath)
        assertEquals(dummySeriesList.data?.size, seriesEntities?.size)
    }

}