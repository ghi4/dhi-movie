package com.dhimas.dhiflix.ui.series

import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.databinding.FragmentSeriesBinding
import com.dhimas.dhiflix.ui.BannerAdapter
import com.dhimas.dhiflix.core.utils.Utils.showSnackBar
import com.dhimas.dhiflix.core.utils.Utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.viewmodel.ext.android.viewModel

class SeriesFragment : Fragment() {

    private lateinit var binding: FragmentSeriesBinding
    private lateinit var seriesAdapter: SeriesAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: SeriesViewModel by viewModel()
    private var currentPage = 1
    private var maxPage = 6
    private var lastBottomLocation = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setPage(currentPage)

        setupUI()
        viewModelObserver()
    }

    private fun setupUI() {
        bottomNavigationView = requireActivity().findViewById(R.id.nav_view)

        //Prevent re-shimmer
        if (viewModel.getIsAlreadyShimmer())
            stopShimmer()
        else
            startShimmer()

        seriesAdapter = SeriesAdapter()
        bannerAdapter = BannerAdapter(requireContext())

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
        with(binding) {
            rvSeries.layoutManager = GridLayoutManager(context, spanCount)
            rvSeries.hasFixedSize()
            rvSeries.adapter = seriesAdapter
            rvSeries.isNestedScrollingEnabled = false

            vpSeriesBanner.adapter = bannerAdapter
            dotsIndicatorSeries.setViewPager2(vpSeriesBanner)

            nestedScrollSeries.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                    val height = (v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight ?: 0)

                    if (scrollY == height && scrollY > lastBottomLocation) {
                        if (currentPage < maxPage) {
                            viewModel.setPage(++currentPage)
                            lastBottomLocation = scrollY
                            showToast(requireContext(), getString(R.string.load_more))
                        } else {
                            showToast(requireContext(), getString(R.string.max_page))
                        }
                    }
                })
        }
    }

    private fun viewModelObserver() {
        viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
            when (seriesList) {
                is Resource.Loading -> {
                    if (lastBottomLocation == 0)
                        startShimmer()
                }

                is Resource.Success -> {
                    if (seriesList.data != null && seriesList.data.isNotEmpty()) {
                        seriesAdapter.addSeries(seriesList.data as ArrayList<Show>)
                        seriesAdapter.notifyDataSetChanged()

                        bannerAdapter.clearBanner()
                        for (i in 0..4)
                            seriesList.data[i].let { bannerAdapter.addBanner(it) }
                        bannerAdapter.notifyDataSetChanged()

                        stopShimmer()
                    } else {
                        showToast(requireContext(), getString(R.string.no_series_found))
                        showSnackBar(requireView(), getString(R.string.do_you_want_retry)) {
                            viewModel.refresh()
                        }
                    }
                    viewModel.setAlreadyShimmer()
                }

                is Resource.Error -> {
                    showSnackBar(
                        bottomNavigationView,
                        seriesList.message ?: getString(R.string.unknown_error)
                    ) {
                        viewModel.refresh()
                    }
                }
            }
        })
    }

    private fun startShimmer() {
        with(binding) {
            shimmerLayoutSeries.startShimmer()
            shimmerLayoutSeries.visibility = View.VISIBLE
        }
    }

    private fun stopShimmer() {
        with(binding) {
            shimmerLayoutSeries.stopShimmer()
            shimmerLayoutSeries.visibility = View.GONE
            tvSeriesPopularTitle.visibility = View.VISIBLE
        }
    }

}