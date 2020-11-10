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
        val dummyMovieList = Resource.success(DummyData.generateDummyMovies())
        val movies = MutableLiveData<Resource<List<ShowEntity>>>()
        movies.value = dummyMovieList

        `when`(movieRepository.getMovieList()).thenReturn(movies)
        val movieEntities = viewModel.getMovies().value?.data
        verify(movieRepository).getMovieList()

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
        verify(observer).onChanged(dummyMovieList)
    }

}