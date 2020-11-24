package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var isAlreadyShimmer: Boolean = false
    private var page = MutableLiveData<Int>()
    private var seriesList = page.switchMap {
        showRepository.getSeriesList(it)
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setPage(page: Int) {
        this.page.postValue(page)
    }

    fun getIsAlreadyShimmer() = isAlreadyShimmer

    fun getSeries(): LiveData<Resource<List<ShowEntity>>> = seriesList

    fun refresh() {
        page.postValue(page.value)
    }

}