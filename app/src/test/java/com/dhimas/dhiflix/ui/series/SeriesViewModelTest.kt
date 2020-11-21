package com.dhimas.dhiflix.ui.series

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
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
internal class SeriesViewModelTest {
    private lateinit var viewModel: SeriesViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var showRepository: ShowRepository

    @Mock
    private lateinit var observer: Observer<Resource<List<ShowEntity>>>

    @Before
    fun setUp() {
        viewModel = SeriesViewModel(showRepository)
    }

    @Test
    fun getSeriesList() {
        val page = 1
        val dummySeriesList =
            Resource.success(DummyData.generateDummySeries())

        val series = MutableLiveData<Resource<List<ShowEntity>>>()
        series.value = dummySeriesList

        `when`(showRepository.getSeriesList(page)).thenReturn(series)
        viewModel.setPage(page)
        viewModel.getSeries().observeForever(observer)
        verify(observer).onChanged(dummySeriesList)

        val seriesEntities = viewModel.getSeries().value?.data
        verify(showRepository).getSeriesList(page)

        val dummySeries = dummySeriesList.data?.get(0)
        val seriesEntity = seriesEntities?.get(0)

        assertNotNull(seriesEntity)
        assertNotNull(seriesEntity?.id)
        assertNotNull(seriesEntity?.title)
        assertNotNull(seriesEntity?.releaseDate)
        assertNotNull(seriesEntity?.overview)
        assertNotNull(seriesEntity?.posterPath)
        assertNotNull(seriesEntity?.backdropPath)

        assertEquals(dummySeries, seriesEntity)
        assertEquals(dummySeries?.id, seriesEntity?.id)
        assertEquals(dummySeries?.title, seriesEntity?.title)
        assertEquals(dummySeries?.releaseDate, seriesEntity?.releaseDate)
        assertEquals(dummySeries?.overview, seriesEntity?.overview)
        assertEquals(dummySeries?.posterPath, seriesEntity?.posterPath)
        assertEquals(dummySeries?.backdropPath, seriesEntity?.backdropPath)
        assertEquals(dummySeriesList.data?.size, seriesEntities?.size)
    }
}