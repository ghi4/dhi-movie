package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var refreshTrigger = MutableLiveData(Unit)
    private var seriesList = refreshTrigger.switchMap {
        showRepository.getSeriesList()
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getSeries(): LiveData<Resource<PagedList<ShowEntity>>> = seriesList

    fun refresh() {
        refreshTrigger.value = Unit
    }

}