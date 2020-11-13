package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository

class SearchViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun search(keyword: String) = showRepository.searchMovie(keyword)
}