package com.dhimas.dhiflix.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
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
class FavoriteFragmentTest {

    private lateinit var viewModel: FavoriteViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<ShowEntity>>>

    @Mock
    private lateinit var pagedList: PagedList<ShowEntity>

    @Before
    fun setUp() {
        viewModel = FavoriteViewModel(showRepository)
    }

    @Test
    fun getFavoriteMovie() {
        val dummyDataMovie = Resource.success(pagedList)
        `when`(dummyDataMovie.data?.size).thenReturn(10)

        val movies = MutableLiveData<Resource<PagedList<ShowEntity>>>()
        movies.value = dummyDataMovie

        `when`(showRepository.getFavoriteMovieList()).thenReturn(movies)
        val movieEntities = viewModel.getFavoriteMovies().value?.data
        verify(showRepository).getFavoriteMovieList()

        assertNotNull(movieEntities)
        assertEquals(10, dummyDataMovie.data?.size)

        viewModel.getFavoriteMovies().observeForever(observer)
        verify(observer).onChanged(dummyDataMovie)
    }

    @Test
    fun getFavoriteSeries() {
        val dummyDataSeries = Resource.success(pagedList)
        `when`(dummyDataSeries.data?.size).thenReturn(10)

        val series = MutableLiveData<Resource<PagedList<ShowEntity>>>()
        series.value = dummyDataSeries

        `when`(showRepository.getFavoriteSeriesList()).thenReturn(series)
        val seriesEntities = viewModel.getFavoriteSeries().value?.data
        verify(showRepository).getFavoriteSeriesList()

        assertNotNull(seriesEntities)
        assertEquals(10, dummyDataSeries.data?.size)

        viewModel.getFavoriteSeries().observeForever(observer)
        verify(observer).onChanged(dummyDataSeries)
    }


}