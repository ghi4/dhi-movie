package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun getShowEntityById(show_id: String, show_type: String): LiveData<ShowEntity> {
        return when (show_type) {
            DetailActivity.EXTRA_FROM_MOVIES -> {
                getMovieEntityById(show_id)
            }
            DetailActivity.EXTRA_FROM_SERIES -> {
                getSeriesEntityById(show_id)
            }
            else -> {
                throw Throwable("Unknown type of show: $show_type")
            }
        }
    }

    fun getShowList(show_type: String): LiveData<List<ShowEntity>> {
        return when (show_type) {
            DetailActivity.EXTRA_FROM_MOVIES -> {
                getMovies()
            }
            DetailActivity.EXTRA_FROM_SERIES -> {
                getSeries()
            }
            else -> {
                throw Throwable("Unknown type of show: $show_type")
            }
        }
    }

    private fun getMovieEntityById(show_id: String): LiveData<ShowEntity> =
        showRepository.getMovieDetail(show_id)

    private fun getSeriesEntityById(show_id: String): LiveData<ShowEntity> =
        showRepository.getSeriesDetail(show_id)

    private fun getMovies(): LiveData<List<ShowEntity>> = showRepository.getMovieList()

    private fun getSeries(): LiveData<List<ShowEntity>> = showRepository.getSeriesList()
}