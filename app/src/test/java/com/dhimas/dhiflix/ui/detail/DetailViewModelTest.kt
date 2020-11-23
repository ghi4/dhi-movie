package com.dhimas.dhiflix.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Const
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
internal class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var showId: String

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<ShowEntity>>

    @Mock
    private lateinit var observerList: Observer<Resource<List<ShowEntity>>>

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

        `when`(showRepository.getSeriesDetail(showId)).thenReturn(series)

        viewModel.setDoubleTrigger(showId, Const.SERIES_TYPE)
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

    @Test
    fun getSimilarMovieList() {
        val dummyMovieList = Resource.success(DummyData.generateDummyMovies())
        val movie = MutableLiveData<Resource<List<ShowEntity>>>()
        movie.value = dummyMovieList

        `when`(showRepository.getSimilarMovieList(showId)).thenReturn(movie)

        viewModel.setDoubleTrigger(showId, Const.MOVIE_TYPE)
        viewModel.getSimilarList().observeForever(observerList)
        verify(observerList).onChanged(dummyMovieList)

        val movieEntities = viewModel.getSimilarList().value?.data
        verify(showRepository).getSimilarMovieList(showId)

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
    fun getPopularMovieList() {
        val page = 1
        val dummyMovieList = Resource.success(DummyData.generateDummyMovies())
        val movie = MutableLiveData<Resource<List<ShowEntity>>>()
        movie.value = dummyMovieList

        `when`(showRepository.getMovieList(page)).thenReturn(movie)

        viewModel.setDoubleTrigger(showId, Const.MOVIE_TYPE)
        viewModel.setListEmptyTrigger()
        viewModel.getPopularList().observeForever(observerList)
        verify(observerList).onChanged(dummyMovieList)

        val movieEntities = viewModel.getPopularList().value?.data
        verify(showRepository).getMovieList(page)

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
}