package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.ShowRepository
import com.dhimas.dhiflix.data.source.local.ShowEntity

class SeriesViewModel(private val showRepository: ShowRepository) : ViewModel() {

    fun getSeries(): LiveData<List<ShowEntity>> = showRepository.getSeriesList()

}