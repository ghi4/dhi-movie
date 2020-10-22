package com.dhimas.dhiflix.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
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
            val factory = ViewModelFactory.getInstance()
            val viewModel = ViewModelProvider(this, factory)[SeriesViewModel::class.java]

            val adapter = SeriesAdapter()

            viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
                adapter.setSeries(seriesList as ArrayList<ShowEntity>)
                adapter.notifyDataSetChanged()
            })

            rv_series.layoutManager = GridLayoutManager(context, 3)
            rv_series.hasFixedSize()
            rv_series.adapter = adapter
        }

    }
}