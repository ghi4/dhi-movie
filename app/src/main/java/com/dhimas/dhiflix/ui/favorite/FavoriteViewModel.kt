package com.dhimas.dhiflix.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class FavoriteViewModel(private val showRepository: ShowRepository) : ViewModel() {
    lateinit var movieList: LiveData<Resource<PagedList<ShowEntity>>>
    lateinit var seriesList: LiveData<Resource<PagedList<ShowEntity>>>

    fun getFavoriteMovies(): LiveData<Resource<PagedList<ShowEntity>>> {
        if(!::movieList.isInitialized)
            movieList = showRepository.getFavoriteMovieList()

        return movieList
    }

    fun getFavoriteSeries(): LiveData<Resource<PagedList<ShowEntity>>> {
        if(!::seriesList.isInitialized)
            seriesList = showRepository.getFavoriteSeriesList()

        return seriesList
    }

}