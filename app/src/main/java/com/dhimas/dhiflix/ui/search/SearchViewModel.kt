package com.dhimas.dhiflix.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*


@FlowPreview
@ExperimentalCoroutinesApi
class SearchViewModel(private val showUseCase: ShowUseCase) : ViewModel() {

    val queryChannel = ConflatedBroadcastChannel<String>()

    private val searchQuery = queryChannel.asFlow()
        .debounce(500L)
        .distinctUntilChanged()
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            it
        }
        .asLiveData()

    private val movieList =
        searchQuery.switchMap {
            showUseCase.searchMovie(it).asLiveData()
        }

    private val seriesList =
        searchQuery.switchMap {
            showUseCase.searchSeries(it).asLiveData()
        }

    fun getSeries(): LiveData<Resource<List<Show>>> = seriesList

    fun getMovies(): LiveData<Resource<List<Show>>> = movieList

}