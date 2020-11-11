package com.dhimas.dhiflix.ui.search.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.search_series_fragment.*

class SearchSeriesFragment : Fragment() {

    private lateinit var viewModel: SearchSeriesViewModel

    companion object {
        private var keyword: String? = null

        fun newInstance(keyword: String?): SearchSeriesFragment {
            val fragment = SearchSeriesFragment()
            Companion.keyword = keyword

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_series_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(SearchSeriesViewModel::class.java)

        val seriesAdapter = SeriesAdapter()

        keyword?.let {
            viewModel.searchSeries(it).observe(viewLifecycleOwner, { seriesList ->
                if(!seriesList.data.isNullOrEmpty()){
                    seriesAdapter.setSeries(seriesList.data as ArrayList<ShowEntity>)
                    seriesAdapter.notifyDataSetChanged()
                }
            })
        }

        rv_search_series.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_series.hasFixedSize()
        rv_search_series.adapter = seriesAdapter
    }

}