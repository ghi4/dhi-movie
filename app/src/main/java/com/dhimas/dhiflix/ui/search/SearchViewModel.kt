package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var searchQuery = MutableLiveData<String>()

    fun searchSeries(keyword: String): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.searchSeries(keyword)

    fun searchMovie(keyword: String): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.searchMovie(keyword)
}