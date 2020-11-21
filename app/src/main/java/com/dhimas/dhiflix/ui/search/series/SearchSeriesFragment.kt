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
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_series_fragment.*

class SearchSeriesFragment : Fragment() {
    private val vm: SearchViewModel by viewModels({ requireParentFragment() })
    private lateinit var seriesAdapter: SeriesAdapter

//    //TEKNIK 2
//    companion object{
//        private lateinit var viewModel: SearchSeriesViewModel
//
//        fun setSearch(search: String){
//            viewModel.setSearch(search)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_series_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        seriesAdapter = SeriesAdapter()

//        //TEKNIK 2
//        val factory = ViewModelFactory.getInstance(requireContext())
//        viewModel = ViewModelProvider(this, factory)[SearchSeriesViewModel::class.java]
//        viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->

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

        rv_search_series.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_series.hasFixedSize()
        rv_search_series.adapter = seriesAdapter
    }

    private fun setViewVisibility(
        loading: Boolean,
        rvSeries: Boolean,
        ivIllustration: Boolean,
        tvInfo: Boolean
    ) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        rv_search_series.visibility = if (rvSeries) View.VISIBLE else View.INVISIBLE
        iv_series_illustration.visibility = if (ivIllustration) View.VISIBLE else View.INVISIBLE
        tv_series_info.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
    }

    private fun setInfoImageAndMessage(image: Int, message: String) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(iv_series_illustration)
        tv_series_info.text = message
    }

    override fun onResume() {
        super.onResume()

        if (!vm.getSearchQuery().value.isNullOrEmpty())
            vm.triggerSeries()
    }


//    override fun onPause() {
//        super.onPause()
//
//        if(!vm.getSearchQuery().value.isNullOrEmpty())
//            vm.triggerMovie()
//    }

}