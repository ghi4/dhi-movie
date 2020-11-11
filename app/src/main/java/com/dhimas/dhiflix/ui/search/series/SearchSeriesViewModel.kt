package com.dhimas.dhiflix.ui.search.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchSeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun searchSeries(keyword: String): LiveData<Resource<List<ShowEntity>>> = showRepository.searchSeries(keyword)

}