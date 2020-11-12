package com.dhimas.dhiflix.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.utils.PagedListUtil
import com.dhimas.dhiflix.vo.Resource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<PagedList<ShowEntity>>>

    @Before
    fun setUp() {
        viewModel = MovieViewModel(showRepository)
    }

    @Test
    fun getMovieList() {
        val dummyMovieList =
            Resource.success(PagedListUtil.mockPagedList(DummyData.generateDummyMovies()))

        val movie = MutableLiveData<Resource<PagedList<ShowEntity>>>()
        movie.value = dummyMovieList

        `when`(showRepository.getMovieList()).thenReturn(movie)
        val movieEntities = viewModel.getMovies().value?.data
        Mockito.verify(showRepository).getMovieList()

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

        viewModel.getMovies().observeForever(observer)
        Mockito.verify(observer).onChanged(dummyMovieList)
    }

}