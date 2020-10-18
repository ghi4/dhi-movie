package com.dhimas.dhiflix.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.utils.DummyData

class DashboardViewModel : ViewModel() {

    fun getSeries(): ArrayList<MovieEntity> = DummyData.generateDummySeries()
}