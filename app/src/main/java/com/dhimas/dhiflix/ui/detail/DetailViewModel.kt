package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    private lateinit var showEntity: LiveData<Resource<ShowEntity>>
    private lateinit var showList: LiveData<Resource<PagedList<ShowEntity>>>

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getShowEntityById(show_id: String, show_type: Int): LiveData<Resource<ShowEntity>> {
        if (!::showEntity.isInitialized) {
            showEntity = when (show_type) {
                Constant.MOVIE_TYPE -> {
                    getMovieEntityById(show_id)
                }
                else -> {
                    getSeriesEntityById(show_id)
                }
            }
        }

        return showEntity
    }

    fun getShowList(show_type: Int, show_id: String): LiveData<Resource<PagedList<ShowEntity>>> {
        if (!::showList.isInitialized) {
            showList = when (show_type) {
                Constant.MOVIE_TYPE -> {
                    getMovies(show_id)
                }
                else -> {
                    getSeries(show_id)
                }
            }
        }

        return showList
    }

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
}