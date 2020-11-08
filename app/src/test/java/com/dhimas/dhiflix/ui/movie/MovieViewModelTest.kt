package com.dhimas.dhiflix.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import com.dhimas.dhiflix.vo.Resource
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
internal class MovieViewModelTest {
    private lateinit var viewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<List<ShowEntity>>>

    @Before
    fun setUp() {
        viewModel = MovieViewModel(movieRepository)
    }

    @Test
    fun getMovieList() {
        val dummyMovie = Resource.success(DummyData.generateDummyMovies())
        val movies = MutableLiveData<Resource<List<ShowEntity>>>()
        movies.value = dummyMovie

        `when`(movieRepository.getMovieList()).thenReturn(movies)
        val movieEntity = viewModel.getMovies().value?.data
        verify(movieRepository).getMovieList()

        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.get(0)?.id)
        assertNotNull(movieEntity?.get(0)?.title)
        assertNotNull(movieEntity?.get(0)?.releaseDate)
        assertNotNull(movieEntity?.get(0)?.overview)
        assertNotNull(movieEntity?.get(0)?.posterPath)
        assertNotNull(movieEntity?.get(0)?.backdropPath)

        assertEquals(dummyMovie.data?.size, movieEntity?.size)
        assertEquals(dummyMovie.data?.get(0), movieEntity?.get(0))
        assertEquals(dummyMovie.data?.get(0)?.id, movieEntity?.get(0)?.id)
        assertEquals(dummyMovie.data?.get(0)?.title, movieEntity?.get(0)?.title)
        assertEquals(dummyMovie.data?.get(0)?.releaseDate, movieEntity?.get(0)?.releaseDate)
        assertEquals(dummyMovie.data?.get(0)?.overview, movieEntity?.get(0)?.overview)
        assertEquals(dummyMovie.data?.get(0)?.posterPath, movieEntity?.get(0)?.posterPath)
        assertEquals(dummyMovie.data?.get(0)?.backdropPath, movieEntity?.get(0)?.backdropPath)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }

}