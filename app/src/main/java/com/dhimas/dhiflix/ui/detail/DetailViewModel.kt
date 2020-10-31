package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getShowEntityById(show_id: String, show_type: String): LiveData<Resource<ShowEntity>> {
        return when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    getMovieEntityById(show_id)
                }
                else -> {
                    getSeriesEntityById(show_id)
                }
            }
    }

    fun getShowList(show_type: String): LiveData<Resource<List<ShowEntity>>> {
        return when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    getMovies()
                }
                else -> {
                    getSeries()
                }
            }
    }

    private fun getMovieEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getMovieDetail(show_id)

    private fun getSeriesEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getSeriesDetail(show_id)

    private fun getMovies(): LiveData<Resource<List<ShowEntity>>> = showRepository.getMovieList()

    private fun getSeries(): LiveData<Resource<List<ShowEntity>>> = showRepository.getSeriesList()
}