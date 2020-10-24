package com.dhimas.dhiflix.ui.series

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.DummyData
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
internal class SeriesViewModelTest {

    private lateinit var viewModel: SeriesViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var seriesRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<List<ShowEntity>>

    @Before
    fun setUp() {
        viewModel = SeriesViewModel(seriesRepository)
    }

    @Test
    fun getSeriesList() {
        val dummySeries = DummyData.generateDummySeries()
        val series = MutableLiveData<List<ShowEntity>>()
        series.value = dummySeries

        `when`(seriesRepository.getSeriesList()).thenReturn(series)

        val seriesEntity = viewModel.getSeries().value
        verify(seriesRepository).getSeriesList()

        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.get(0)?.id)
        assertNotNull(seriesEntity?.get(0)?.title)
        assertNotNull(seriesEntity?.get(0)?.releaseDate)
        assertNotNull(seriesEntity?.get(0)?.overview)
        assertNotNull(seriesEntity?.get(0)?.posterPath)
        assertNotNull(seriesEntity?.get(0)?.backdropPath)

        assertEquals(dummySeries.size, seriesEntity?.size)
        assertEquals(dummySeries[0], seriesEntity?.get(0))
        assertEquals(dummySeries[0].id, seriesEntity?.get(0)?.id)
        assertEquals(dummySeries[0].title, seriesEntity?.get(0)?.title)
        assertEquals(dummySeries[0].releaseDate, seriesEntity?.get(0)?.releaseDate)
        assertEquals(dummySeries[0].overview, seriesEntity?.get(0)?.overview)
        assertEquals(dummySeries[0].posterPath, seriesEntity?.get(0)?.posterPath)
        assertEquals(dummySeries[0].backdropPath, seriesEntity?.get(0)?.backdropPath)

        viewModel.getSeries().observeForever(observer)
        verify(observer).onChanged(dummySeries)

    }
}