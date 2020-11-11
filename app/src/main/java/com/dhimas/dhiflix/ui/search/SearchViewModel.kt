package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun searchMovie(keyword: String): LiveData<Resource<List<ShowEntity>>> = showRepository.searchMovie(keyword)

    fun searchSeries(keyword: String): LiveData<Resource<List<ShowEntity>>> = showRepository.searchSeries(keyword)

}