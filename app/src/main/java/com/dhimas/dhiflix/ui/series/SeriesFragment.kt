package com.dhimas.dhiflix.ui.series

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.SliderAdapter
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_series.*

class SeriesFragment : Fragment() {
    private lateinit var viewModel: SeriesViewModel
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null && context != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[SeriesViewModel::class.java]
            seriesAdapter = SeriesAdapter()
            sliderAdapter = SliderAdapter(requireActivity())

            //Minimum time for shimmer
            val minShimmerTime =
                if (!viewModel.isAlreadyShimmer) Constant.MINIMUM_SHIMMER_TIME else 0

            //Prevent re-shimmer when rotating phone
            if (viewModel.isAlreadyShimmer) {
                stopShimmer()
            }

            //Delay loading for shimmer
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
                viewModel.setAlreadyShimmer()
            }, minShimmerTime)

            //Change grid layout spanCount when Landscape/Portrait
            val phoneOrientation = requireActivity().resources.configuration.orientation
            val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
            rv_series.layoutManager = GridLayoutManager(context, spanCount)
            rv_series.hasFixedSize()
            rv_series.adapter = seriesAdapter
        }
    }

    private fun viewModelObserve() {
        if (view != null) {
            viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
                if (seriesList != null) {
                    when (seriesList.status) {
                        Status.LOADING -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            seriesAdapter.submitList(seriesList.data)
                            seriesAdapter.notifyDataSetChanged()

                            if(seriesList.data != null) {
                                sliderAdapter.deleteShow()
                                for (item in seriesList.data) {
                                    sliderAdapter.addShow(item)
                                }
                                vp_series_slider.adapter = sliderAdapter
                                dots_indicator_series.setViewPager2(vp_series_slider)
                                textView4.visibility = View.VISIBLE
                            }

                            viewModel.setAlreadyShimmer()
                            stopShimmer()
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                stopShimmer()
            })
        }
    }

    private fun stopShimmer() {
        seriesShimmerLayout.stopShimmer()
        seriesShimmerLayout.visibility = View.GONE
    }
}