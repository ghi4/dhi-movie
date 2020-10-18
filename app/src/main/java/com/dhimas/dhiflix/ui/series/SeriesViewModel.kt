package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class SeriesViewModel : ViewModel() {

    fun getSeries(): ArrayList<MovieEntity> = DummyData.generateDummySeries()
}