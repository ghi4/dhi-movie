package com.dhimas.dhiflix.ui.series

import androidx.lifecycle.ViewModel
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.DummyData

class SeriesViewModel : ViewModel() {
    private var seriesList = ArrayList<ShowEntity>()

    fun getSeries(): ArrayList<ShowEntity> {

        //Load data when seriesList is empty
        //Prevent re-load when rotating phone
        if (seriesList.isEmpty()) {
            val series = DummyData.generateDummySeries()
            seriesList.addAll(series)
        }

        return seriesList
    }
}