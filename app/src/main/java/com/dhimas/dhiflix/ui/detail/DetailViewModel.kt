package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.DoubleTrigger
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var doubleTrigger = MutableLiveData<DoubleTrigger>()
    private var listEmptyTrigger = MutableLiveData<Unit>()

    private var showEntity = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showRepository.getMovieDetail(it.showId)
            else ->
                showRepository.getSeriesDetail(it.showId)
        }
    }

    private var similarList = doubleTrigger.switchMap {
        when (it.showType) {
            Const.MOVIE_TYPE ->
                showRepository.getSimilarMovieList(it.showId)
            else ->
                showRepository.getSimilarSeriesList(it.showId)
        }
    }

    private var popularList = listEmptyTrigger.switchMap {
        val showType = doubleTrigger.value?.showType
        val page = 1

        when (showType) {
            Const.MOVIE_TYPE ->
                showRepository.getMovieList(page)
            else ->
                showRepository.getSeriesList(page)
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

    fun getShowEntity(): LiveData<Resource<ShowEntity>> = showEntity

    fun getSimilarList(): LiveData<Resource<List<ShowEntity>>> = similarList

    fun getPopularList(): LiveData<Resource<List<ShowEntity>>> = popularList

    fun setFavorite(showEntity: ShowEntity) {
        showRepository.setFavorite(showEntity)
    }

}