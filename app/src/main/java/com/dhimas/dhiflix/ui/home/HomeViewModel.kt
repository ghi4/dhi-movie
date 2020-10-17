package com.dhimas.dhiflix.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class HomeViewModel : ViewModel() {

    fun getMovies(): ArrayList<MovieEntity> = DummyData.generateDummyMovies()
}