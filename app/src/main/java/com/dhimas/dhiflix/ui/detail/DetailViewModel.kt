package com.dhimas.dhiflix.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.DoubleTrigger
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var doubleTrigger = MutableLiveData<DoubleTrigger>()
    private var listEmptyTrigger = MutableLiveData<Unit>()

    private var showEntity = doubleTrigger.switchMap {
        when (it.showType) {
            Constant.MOVIE_TYPE ->
                showRepository.getMovieDetail(it.showId)
            else ->
                showRepository.getSeriesDetail(it.showId)
        }
    }

    private var similarList = showEntity.switchMap {
        Log.d("WKVM", "IN VM SIMILAR")
        Log.d("WKVM", "IN VM SIMILAR ID: ${it.data?.id}")
        Log.d("WKVM", "IN VM SIMILAR TY: ${it.data?.showType}")

        when (doubleTrigger.value?.showType) {
            Constant.MOVIE_TYPE ->
                showRepository.getSimilarMovieList("400160")
            else ->
                showRepository.getSimilarSeriesList("400160")
        }
    }

    private var popularList = doubleTrigger.switchMap {
        Log.d("WKVM", "IN VM POP")
        val showType = doubleTrigger.value?.showType
        Log.d("WKVM", "IN VM POP: " + showType)
        val page = 1
        when (Constant.MOVIE_TYPE) {
            Constant.MOVIE_TYPE ->
                showRepository.getMovieList(page)
            else ->
                showRepository.getSeriesList(page)
        }
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setDoubleTrigger(show_id: String, show_type: Int) {
        doubleTrigger.postValue(DoubleTrigger(show_id, show_type))
    }

    fun listEmptyTrigger() {
        listEmptyTrigger.postValue(Unit)
    }

    fun getShowEntityById(): LiveData<Resource<ShowEntity>> = showEntity

    fun getSimilarList(): LiveData<Resource<List<ShowEntity>>> = similarList

    fun getPopularList(): LiveData<Resource<List<ShowEntity>>> = popularList

    fun setFavorite(showEntity: ShowEntity) {
        showRepository.setFavorite(showEntity)
    }

}