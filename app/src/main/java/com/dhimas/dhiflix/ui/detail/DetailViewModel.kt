package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    private var showEntity: LiveData<ShowEntity>? = null
    private var showList: LiveData<List<ShowEntity>>? = null
    var isAlreadyShimmer: Boolean = false

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getShowEntityById(show_id: String, show_type: String): LiveData<ShowEntity> {

        //Prevent re-load when rotating phone
        if (showEntity == null) {
            when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    showEntity = getMovieEntityById(show_id)
                }
                DetailActivity.EXTRA_FROM_SERIES -> {
                    showEntity = getSeriesEntityById(show_id)
                }
            }
        }

        return showEntity as LiveData<ShowEntity>
    }

    fun getShowList(show_type: String): LiveData<List<ShowEntity>> {

        //Prevent re-load when rotating phone
        if (showList == null) {
            when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    showList = getMovies()
                }
                DetailActivity.EXTRA_FROM_SERIES -> {
                    showList = getSeries()
                }
            }
        }

        return showList as LiveData<List<ShowEntity>>
    }

    private fun getMovieEntityById(show_id: String): LiveData<ShowEntity> =
        showRepository.getMovieDetail(show_id)

    private fun getSeriesEntityById(show_id: String): LiveData<ShowEntity> =
        showRepository.getSeriesDetail(show_id)

    private fun getMovies(): LiveData<List<ShowEntity>> = showRepository.getMovieList()

    private fun getSeries(): LiveData<List<ShowEntity>> = showRepository.getSeriesList()
}