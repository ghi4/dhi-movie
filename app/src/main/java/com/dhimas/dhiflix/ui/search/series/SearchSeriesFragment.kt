package com.dhimas.dhiflix.ui.search.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.databinding.FragmentSearchSeriesBinding
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.squareup.picasso.Picasso

class SearchSeriesFragment : Fragment() {
    private lateinit var binding: FragmentSearchSeriesBinding
    private lateinit var seriesAdapter: SeriesAdapter
    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
            when (seriesList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = false, tvInfo = false)
                }

                is Resource.Success -> {
                    seriesAdapter.addSeries(seriesList.data as ArrayList<Show>)
                    seriesAdapter.notifyDataSetChanged()

                    if (seriesList.data.isNullOrEmpty()) {
                        setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                        setInfoImageAndMessage(
                            R.drawable.undraw_not_found_60pq,
                            getString(R.string.no_series_found)
                        )
                    } else {
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }

                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    setInfoImageAndMessage(
                        R.drawable.undraw_signal_searching_bhpc,
                        seriesList.message ?: getString(R.string.unknown_error)
                    )
                }
            }
        })
    }

    private fun setupUI() {
        seriesAdapter = SeriesAdapter()

        with(binding) {
            rvSearchSeries.layoutManager = GridLayoutManager(requireContext(), 3)
            rvSearchSeries.hasFixedSize()
            rvSearchSeries.adapter = seriesAdapter
        }
    }

    private fun setViewVisibility(loading: Boolean, ivInfo: Boolean, tvInfo: Boolean) {
        with(binding) {
            pbSearchSeries.visibility = if (loading) View.VISIBLE else View.GONE
            ivSearchSeriesInfo.visibility = if (ivInfo) View.VISIBLE else View.INVISIBLE
            tvSearchSeriesInfo.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setInfoImageAndMessage(image: Int, message: String) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(binding.ivSearchSeriesInfo)
        binding.tvSearchSeriesInfo.text = message
    }

}