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
        assertEquals(12, movieEntity?.size)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }

}