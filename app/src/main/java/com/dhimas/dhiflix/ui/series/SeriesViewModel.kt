package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class SeriesViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    private var isAlreadyShimmer: Boolean = false
    private var page = MutableLiveData<Int>()
    private var seriesList = page.switchMap {
        showUseCase.getSeriesList(it)
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setPage(page: Int) {
        this.page.postValue(page)
    }

    fun getIsAlreadyShimmer() = isAlreadyShimmer

    fun getSeries(): LiveData<Resource<List<Show>>> = seriesList

    fun refresh() {
        page.postValue(page.value)
    }

}