package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.*
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class SeriesViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    //Value if already shimmer or not
    private var isAlreadyShimmer: Boolean = false

    //Used for "Load More"
    private var page = MutableLiveData<Int>()

    //Get series list
    //Triggered when page is set by setPage() or refresh() is called
    private var seriesList = page.switchMap {
        showUseCase.getSeriesList(it).asLiveData()
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