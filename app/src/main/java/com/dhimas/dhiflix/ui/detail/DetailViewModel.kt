package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.vo.Resource

class DetailViewModel(private val showRepository: ShowRepository) : ViewModel() {
    var isAlreadyShimmer: Boolean = false
    lateinit var showEntity: LiveData<Resource<ShowEntity>>
    lateinit var showList: LiveData<Resource<List<ShowEntity>>>

    fun setAlreadyShimmer() {
        isAlreadyShimmer = true
    }

    fun getShowEntityById(show_id: String, show_type: String): LiveData<Resource<ShowEntity>> {
        if(!::showEntity.isInitialized){
            showEntity = when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    getMovieEntityById(show_id)
                }
                else -> {
                    getSeriesEntityById(show_id)
                }
            }
        }

        return showEntity
    }

    fun getShowList(show_type: String): LiveData<Resource<List<ShowEntity>>> {
        if(!::showList.isInitialized){
            showList = when (show_type) {
                DetailActivity.EXTRA_FROM_MOVIES -> {
                    getMovies()
                }
                else -> {
                    getSeries()
                }
            }
        }

        return showList
    }

    private fun getMovieEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getMovieDetail(show_id)

    private fun getSeriesEntityById(show_id: String): LiveData<Resource<ShowEntity>> =
        showRepository.getSeriesDetail(show_id)

    private fun getMovies(): LiveData<Resource<List<ShowEntity>>> = showRepository.getMovieList()

    private fun getSeries(): LiveData<Resource<List<ShowEntity>>> = showRepository.getSeriesList()

    fun setFavorite(showEntity: ShowEntity) {
        showRepository.setFavorite(showEntity)
    }
}