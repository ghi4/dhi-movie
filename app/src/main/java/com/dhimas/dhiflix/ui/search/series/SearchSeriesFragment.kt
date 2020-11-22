package com.dhimas.dhiflix.ui.search.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.FragmentSearchSeriesBinding
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso

class SearchSeriesFragment : Fragment() {
    private val vm: SearchViewModel by viewModels({ requireParentFragment() })
    private lateinit var binding: FragmentSearchSeriesBinding
    private lateinit var seriesAdapter: SeriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        return inflater.inflate(R.layout.fragment_search_series, container, false)

        binding = FragmentSearchSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesAdapter = SeriesAdapter()

        vm.getSeries().observe(viewLifecycleOwner, { seriesList ->
            when (seriesList.status) {
                Status.SUCCESS -> {
                    seriesAdapter.addSeries(seriesList.data as ArrayList<ShowEntity>)
                    seriesAdapter.notifyDataSetChanged()

                    if (!seriesList.data.isNullOrEmpty()) {
                        setViewVisibility(
                            loading = false,
                            rvSeries = true,
                            ivIllustration = false,
                            tvInfo = false
                        )
                    } else {
                        setViewVisibility(
                            loading = false,
                            rvSeries = false,
                            ivIllustration = true,
                            tvInfo = true
                        )
                        setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, "No series found.")
                    }
                }

                Status.LOADING -> {
                    setViewVisibility(
                        loading = true,
                        rvSeries = false,
                        ivIllustration = false,
                        tvInfo = false
                    )
                }

                Status.ERROR -> {
                    setViewVisibility(
                        loading = false,
                        rvSeries = false,
                        ivIllustration = true,
                        tvInfo = true
                    )
                    setInfoImageAndMessage(
                        R.drawable.undraw_not_found_60pq,
                        seriesList.message.toString()
                    )
                }
            }
        })


        with(binding) {
            rvSearchSeries.layoutManager = GridLayoutManager(requireContext(), 3)
            rvSearchSeries.hasFixedSize()
            rvSearchSeries.adapter = seriesAdapter
        }
    }

    private fun setViewVisibility(
        loading: Boolean,
        rvSeries: Boolean,
        ivIllustration: Boolean,
        tvInfo: Boolean
    ) {
        with(binding) {
            progressBar.visibility = if (loading) View.VISIBLE else View.GONE
            rvSearchSeries.visibility = if (rvSeries) View.VISIBLE else View.INVISIBLE
            ivSeriesIllustration.visibility = if (ivIllustration) View.VISIBLE else View.INVISIBLE
            tvSeriesInfo.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
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
            .into(binding.ivSeriesIllustration)
        binding.tvSeriesInfo.text = message
    }

}