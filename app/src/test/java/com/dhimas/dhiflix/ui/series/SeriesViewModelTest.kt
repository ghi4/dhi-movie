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
        assertEquals(12, seriesEntity?.size)

        viewModel.getSeries().observeForever(observer)
        verify(observer).onChanged(dummySeries)

    }
}