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
import kotlinx.android.synthetic.main.search_movie_fragment.*
import kotlinx.android.synthetic.main.search_series_fragment.*
import kotlinx.android.synthetic.main.search_series_fragment.progressBar

class SearchSeriesFragment : Fragment() {
    private lateinit var seriesAdapter: SeriesAdapter

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

        seriesAdapter = SeriesAdapter()

        viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
                when (seriesList.status) {
                    Status.SUCCESS -> {
                        seriesAdapter.submitList(seriesList.data)
                        seriesAdapter.notifyDataSetChanged()

                        if(seriesList.data != null) {
                            setViewVisibility(loading = false, ivIllustration = false, tvInfo = false)
                        }
                        else{
                            setViewVisibility(loading = false, ivIllustration = true, tvInfo = true)
                            setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, "No series found.")
                        }
                    }

                    Status.LOADING -> {
                        setViewVisibility(loading = true, ivIllustration = false, tvInfo = false)
                    }

                    Status.ERROR -> {
                        setViewVisibility(loading = false, ivIllustration = true, tvInfo = true)
                        setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, seriesList.message.toString())
                    }
                }
        })

        rv_search_series.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_series.hasFixedSize()
        rv_search_series.adapter = seriesAdapter
    }

    private fun setViewVisibility(loading: Boolean, ivIllustration: Boolean, tvInfo: Boolean){
        progressBar.visibility = if(loading) View.VISIBLE else View.GONE
        iv_series_illustration.visibility = if(ivIllustration) View.VISIBLE else View.INVISIBLE
        tv_series_info.visibility = if(tvInfo) View.VISIBLE else View.INVISIBLE
    }

    private fun setInfoImageAndMessage(image: Int, message: String){
        val imgSize = 230

        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(imgSize, imgSize)
            .into(iv_series_illustration)
        tv_series_info.text = message
    }

}