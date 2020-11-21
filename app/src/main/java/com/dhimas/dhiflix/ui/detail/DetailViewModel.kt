package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private var doubleTrigger = MutableLiveData<DoubleTrigger>()

    private var showEntity = doubleTrigger.switchMap {
        when (it.showType) {
            Constant.MOVIE_TYPE -> showRepository.getMovieDetail(it.showId)
            else -> showRepository.getSeriesDetail(it.showId)
        }
    }

    private var showList = showEntity.switchMap {
        val showId = it.data?.id ?: "671039"
        when (it.data?.show_type) {
            Constant.MOVIE_TYPE -> {
                showRepository.getSimilarMovieList(showId)
            }
            else -> showRepository.getSimilarSeriesList(showId)
        }
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setDoubleTrigger(show_id: String, show_type: Int) {
        doubleTrigger.postValue(DoubleTrigger(show_id, show_type))
    }

    fun getShowEntityById(): LiveData<Resource<ShowEntity>> = showEntity

    fun getShowList(): LiveData<Resource<PagedList<ShowEntity>>> = showList

    fun setFavorite(showEntity: ShowEntity) {
        showRepository.setFavorite(showEntity)
    }

    class DoubleTrigger(val showId: String, val showType: Int)
}