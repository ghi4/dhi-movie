package com.dhimas.dhiflix.ui.series

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.SliderAdapter
import com.dhimas.dhiflix.utils.Utils.doneDelay
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.utils.Utils.waitDelay
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_series.*

class SeriesFragment : Fragment() {

    private lateinit var viewModel: SeriesViewModel
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private var page = 1
    private var scrollLocation = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(requireActivity(), factory)[SeriesViewModel::class.java]
        seriesAdapter = SeriesAdapter()
        sliderAdapter = SliderAdapter(requireContext())
        viewModel.setPage(page)

        setupUI()

        //Delay for shimmer animation
        waitDelay()
        val minShimmerTime = getMinShimmerTime(viewModel.isAlreadyShimmer)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserver()
        }, minShimmerTime)
    }

    private fun setupUI() {
        //Prevent re-shimmer
        if (viewModel.isAlreadyShimmer)
            stopShimmer()

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
        rv_series.layoutManager = GridLayoutManager(context, spanCount)
        rv_series.hasFixedSize()
        rv_series.adapter = seriesAdapter
        rv_series.isNestedScrollingEnabled = false

        vp_series_slider.adapter = sliderAdapter
        dots_indicator_series.setViewPager2(vp_series_slider)

        nestedScrollSeries.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == (v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight ?: 0)) {
                    val height = (v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight ?: 0)

                    if (scrollY == height && scrollY > scrollLocation) {
                        if (page < 5) {
                            page++
                            viewModel.setPage(page)
                            scrollLocation = scrollY
                            showToast(requireContext(), "Load more.")
                        } else {
                            showToast(requireContext(), "Max page.")
                        }
                    }
                }
            })
    }

    private fun viewModelObserver() {
        if(view != null) {
            viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
                when (seriesList.status) {
                    Status.LOADING -> {
                        if (scrollLocation == 0)
                            startShimmer()
                    }

                    Status.SUCCESS -> {
                        if (seriesList.data != null) {
                            seriesAdapter.addSeries(seriesList.data as ArrayList<ShowEntity>)
                            seriesAdapter.notifyDataSetChanged()

                            sliderAdapter.sliderEntities.clear()
                            for (i in 0..4)
                                seriesList.data[i].let { sliderAdapter.sliderEntities.add(it) }
                            sliderAdapter.notifyDataSetChanged()

                            textView4.visibility = View.VISIBLE
                            stopShimmer()

                            if (!viewModel.isAlreadyShimmer)
                                doneDelay()
                        } else {
                            showToast(requireContext(), "No Series Found.")
                            showSnackBar(requireView(), "Do you want to retry?") {
                                viewModel.refresh()
                            }
                        }
                        viewModel.setAlreadyShimmer()
                    }

                    Status.ERROR -> {
                        showSnackBar(requireView(), seriesList.message.toString()) {
                            viewModel.refresh()
                        }
                    }
                }
            })
        }
    }

    private fun startShimmer() {
        seriesShimmerLayout.startShimmer()
        seriesShimmerLayout.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        seriesShimmerLayout.stopShimmer()
        seriesShimmerLayout.visibility = View.GONE
    }

}