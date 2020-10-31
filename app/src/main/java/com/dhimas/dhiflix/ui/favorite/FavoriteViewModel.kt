package com.dhimas.dhiflix.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class FavoriteViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun getFavoriteMovies(): LiveData<Resource<PagedList<ShowEntity>>> = showRepository.getFavoriteMovieList()

    fun getFavoriteSeries(): LiveData<Resource<PagedList<ShowEntity>>> = showRepository.getFavoriteSeriesList()
}