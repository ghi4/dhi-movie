package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private lateinit var seriesList: LiveData<Resource<PagedList<ShowEntity>>>

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getSeries(): LiveData<Resource<PagedList<ShowEntity>>> {
        if (!::seriesList.isInitialized)
            seriesList = showRepository.getSeriesList()

        return seriesList
    }

}