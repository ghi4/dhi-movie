package com.dhimas.dhiflix.ui.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
internal class MovieViewModelTest {

    private lateinit var viewModel: MovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var movieRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<List<ShowEntity>>

    @Before
    fun setUp() {
        viewModel = MovieViewModel(movieRepository)
    }

    @Test
    fun getMovieList() {
        val dummyMovie = DummyData.generateDummyMovies()
        val movies = MutableLiveData<List<ShowEntity>>()
        movies.value = dummyMovie

        `when`(movieRepository.getMovieList()).thenReturn(movies)

        val movieEntity = viewModel.getMovies().value
        verify(movieRepository).getMovieList()

        assertNotNull(movieEntity)
        assertNotNull(movieEntity?.get(0)?.id)
        assertNotNull(movieEntity?.get(0)?.title)
        assertNotNull(movieEntity?.get(0)?.releaseDate)
        assertNotNull(movieEntity?.get(0)?.overview)
        assertNotNull(movieEntity?.get(0)?.posterPath)
        assertNotNull(movieEntity?.get(0)?.backdropPath)

        assertEquals(dummyMovie.size, movieEntity?.size)
        assertEquals(dummyMovie[0], movieEntity?.get(0))
        assertEquals(dummyMovie[0].id, movieEntity?.get(0)?.id)
        assertEquals(dummyMovie[0].title, movieEntity?.get(0)?.title)
        assertEquals(dummyMovie[0].releaseDate, movieEntity?.get(0)?.releaseDate)
        assertEquals(dummyMovie[0].overview, movieEntity?.get(0)?.overview)
        assertEquals(dummyMovie[0].posterPath, movieEntity?.get(0)?.posterPath)
        assertEquals(dummyMovie[0].backdropPath, movieEntity?.get(0)?.backdropPath)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }

}