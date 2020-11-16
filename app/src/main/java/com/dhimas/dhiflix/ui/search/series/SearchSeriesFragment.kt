package com.dhimas.dhiflix.ui.search.series

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_series_fragment.*

class SearchSeriesFragment : Fragment() {

    companion object {
        private lateinit var viewModel: SearchViewModel

        fun newInstance(viewModel: SearchViewModel): SearchSeriesFragment {
            val fragment = SearchSeriesFragment()
            Companion.viewModel = viewModel

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_series_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seriesAdapter = SeriesAdapter()
        seriesAdapter.notifyDataSetChanged()

        viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
            Log.d("Kucing", "===IN Series===")
            if (!seriesList.data.isNullOrEmpty()) {
                when (seriesList.status) {
                    Status.SUCCESS -> {
                        seriesAdapter.submitList(seriesList.data)
                        seriesAdapter.notifyDataSetChanged()

                        Log.d("Kucingx", "Size: " + seriesList.data.size)
                        Log.d("Kucingx", "Title: " + seriesList.data[0]?.title)

                        progressBar.visibility = View.GONE
                        iv_series_illustration.visibility = View.GONE
                        tv_series_info.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        iv_series_illustration.visibility = View.GONE
                        tv_series_info.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        val imgSize = 230

                        Picasso.get()
                            .load(R.drawable.undraw_not_found_60pq)
                            .placeholder(R.drawable.backdrop_placeholder)
                            .error(R.drawable.image_error)
                            .resize(imgSize, imgSize)
                            .into(iv_series_illustration)
                        tv_series_info.text = seriesList.message

                        progressBar.visibility = View.GONE
                        iv_series_illustration.visibility = View.VISIBLE
                        tv_series_info.visibility = View.VISIBLE
                    }
                }

            }
        })

        rv_search_series.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_series.hasFixedSize()
        rv_search_series.adapter = seriesAdapter
    }

}