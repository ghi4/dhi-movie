package com.dhimas.dhiflix.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
import com.nhaarman.mockitokotlin2.mock
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.mockito.junit.MockitoJUnitRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

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
        verify<ShowRepository>(movieRepository).getMovieList()

        assertNotNull(movieEntity)
        assertEquals(12, movieEntity?.size)

        viewModel.getMovies().observeForever(observer)
        verify(observer).onChanged(dummyMovie)
    }

}