package com.dhimas.dhiflix.ui.detail

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class DetailViewModel: ViewModel() {

    fun getMovie() = DummyData.generateDummyMovies()

    fun getSeries() = DummyData.generateDummySeries()
}