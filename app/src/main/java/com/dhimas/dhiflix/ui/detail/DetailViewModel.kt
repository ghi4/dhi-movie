package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.core.data.source.local.entity.DoubleTrigger
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.domain.usecase.ShowUseCase

class DetailViewModel(private val showUseCase: ShowUseCase) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var doubleTrigger = MutableLiveData<DoubleTrigger>()
    private var listEmptyTrigger = MutableLiveData<Unit>()

    private var show = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getMovieDetail(it.showId)
            else ->
                showUseCase.getSeriesDetail(it.showId)
        }
    }

    private var similarList = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getSimilarMovieList(it.showId)
            else ->
                showUseCase.getSimilarSeriesList(it.showId)
        }
    }

    private var popularList = listEmptyTrigger.switchMap {
        val showType = doubleTrigger.value?.showType
        val page = 1

        when (showType) {
            Const.MOVIE_TYPE ->
                showUseCase.getMovieList(page)
            else ->
                showUseCase.getSeriesList(page)
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