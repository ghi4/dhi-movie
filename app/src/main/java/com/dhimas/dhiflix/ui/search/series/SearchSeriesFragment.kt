package com.dhimas.dhiflix.ui.search.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
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
        seriesAdapter.notifyDataSetChanged()

        keyword?.let {
            viewModel.searchSeries(it).observe(viewLifecycleOwner, { seriesList ->
                if (!seriesList.data.isNullOrEmpty()) {
                    when (seriesList.status) {
                        Status.SUCCESS -> {
                            seriesAdapter.submitList(seriesList.data)
                            seriesAdapter.notifyDataSetChanged()

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
        }

        rv_search_series.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_series.hasFixedSize()
        rv_search_series.adapter = seriesAdapter
    }

}