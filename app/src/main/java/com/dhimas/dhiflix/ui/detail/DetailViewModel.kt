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

    private lateinit var showEntity: LiveData<Resource<ShowEntity>>
    private lateinit var showList: LiveData<Resource<PagedList<ShowEntity>>>

    private var refreshTrigger = MutableLiveData(Unit)

    var doubleTrigger = MutableLiveData<DoubleTrigger>()

    private var showEntity1 = doubleTrigger.switchMap {
        when (it.showType) {
            Constant.MOVIE_TYPE -> getMovieEntityById(it.showId)
            else -> getSeriesEntityById(it.showId)
        }
    }

    private var showList1 = doubleTrigger.switchMap {
        when (it.showType) {
            Constant.MOVIE_TYPE -> getMovies(it.showId)
            else -> getSeries(it.showId)
        }
    }

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun setDoubleTrigger(show_id: String, show_type: Int) {
        doubleTrigger.postValue(DoubleTrigger(show_id, show_type))
    }

    fun getShowEntityById(): LiveData<Resource<ShowEntity>> = showEntity1

    fun getShowList(): LiveData<Resource<PagedList<ShowEntity>>> = showList1

//    fun getShowEntityById(show_id: String, show_type: Int): LiveData<Resource<ShowEntity>> {
//        if (!::showEntity.isInitialized) {
//            showEntity = when (show_type) {
//                Constant.MOVIE_TYPE -> {
//                    getMovieEntityById(show_id)
//                }
//                else -> {
//                    getSeriesEntityById(show_id)
//                }
//            }
//        }
//
//        return showEntity
//    }
//
//    fun getShowList(show_type: Int, show_id: String): LiveData<Resource<PagedList<ShowEntity>>> {
//        if (!::showList.isInitialized) {
//            showList = when (show_type) {
//                Constant.MOVIE_TYPE -> {
//                    getMovies(show_id)
//                }
//                else -> {
//                    getSeries(show_id)
//                }
//            }
//        }
//
//        return showList
//    }

    private fun getMovieEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getMovieDetail(show_id)

    private fun getSeriesEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getSeriesDetail(show_id)

    private fun getMovies(show_id: String): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.getSimilarMovieList(show_id)

    private fun getSeries(show_id: String): LiveData<Resource<PagedList<ShowEntity>>> =
        showRepository.getSimilarSeriesList(show_id)

    fun setFavorite(showEntity: ShowEntity) {
        showRepository.setFavorite(showEntity)
    }

    class DoubleTrigger(val showId: String, val showType: Int)
}