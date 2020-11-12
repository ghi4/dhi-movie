package com.dhimas.dhiflix.ui.search.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class SearchMovieViewModel(private val showRepository: ShowRepository) : ViewModel() {
    fun searchMovie(keyword: String): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.searchMovie(keyword)
}