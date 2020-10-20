package com.dhimas.dhiflix.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import kotlinx.android.synthetic.main.fragment_dashboard.*

class SeriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            val viewModel = ViewModelProvider(this).get(SeriesViewModel::class.java)
            val series = viewModel.getSeries()

            val adapter = SeriesAdapter()
            adapter.setSeries(series)

            rv_series.layoutManager = GridLayoutManager(context, 3)
            rv_series.hasFixedSize()
            rv_series.adapter = adapter
        }

    }
}