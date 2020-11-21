package com.dhimas.dhiflix.ui.search.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchSeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var searchQuery = MutableLiveData<String>()

    private var seriesList = searchQuery.switchMap {
        showRepository.searchSeries(it.toString())
    }

    fun setSearch(search: String) {
        searchQuery.postValue(search)
    }

    fun getSeries(): LiveData<Resource<List<ShowEntity>>> = seriesList
}