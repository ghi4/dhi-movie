package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.*
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.data.source.local.entity.DoubleTrigger
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase
import com.dhimas.dhiflix.utils.Const

class DetailViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var doubleTrigger = MutableLiveData<DoubleTrigger>()
    private var listEmptyTrigger = MutableLiveData<Unit>()

    private var show = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getMovieDetail(it.showId).asLiveData()
            else ->
                showUseCase.getSeriesDetail(it.showId).asLiveData()
        }
    }

    private var similarList = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getSimilarMovieList(it.showId).asLiveData()
            else ->
                showUseCase.getSimilarSeriesList(it.showId).asLiveData()
        }
    }

    private var popularList = listEmptyTrigger.switchMap {
        val showType = doubleTrigger.value?.showType
        val page = 1

        when (showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getMovieList(page).asLiveData()
            else ->
                showUseCase.getSeriesList(page).asLiveData()
        }
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setShowIdAndType(show_id: String, show_type: Int) {
        doubleTrigger.postValue(DoubleTrigger(show_id, show_type))
    }

    fun setListEmptyTrigger() {
        listEmptyTrigger.postValue(Unit)
    }

    fun getShow(): LiveData<Resource<Show>> = show

    fun getSimilarList(): LiveData<Resource<List<Show>>> = similarList

    fun getPopularList(): LiveData<Resource<List<Show>>> = popularList

    fun setFavorite(show: Show) {
        showUseCase.setFavorite(show)
    }

}