package com.dhimas.dhiflix.ui.series

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.ui.ShowsAdapter
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.databinding.FragmentSeriesBinding
import com.dhimas.dhiflix.ui.BannerAdapter
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.qualifier.named

class SeriesFragment : Fragment() {

    private val scopeId = "SeriesScope"
    private val moduleSeries = getKoin().getOrCreateScope(scopeId, named(Const.VIEW_MODEL))
    private val viewModel: SeriesViewModel by moduleSeries.viewModel(this)

    private var _binding: FragmentSeriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var seriesAdapter: ShowsAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private var currentPage = 1
    private var maxPage = 6
    private var lastBottomLocation = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Trigger to load data
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

        bannerAdapter = BannerAdapter(requireContext())
        seriesAdapter = ShowsAdapter()

        //OnClick go to DetailActivity
        seriesAdapter.onItemClick = { selectedShow ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }

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

            //Trigger "Load More" when at bottom and prevent double load request
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
                    startShimmer()
                }

                is Resource.Success -> {
                    val data = seriesList.data as ArrayList<Show>
                    if (!data.isNullOrEmpty()) {
                        seriesAdapter.setList(data)
                        bannerAdapter.setBanner(data)
                        stopShimmer()
                    } else {
                        showToast(requireContext(), getString(R.string.no_series_found))
                        //Show snackbar for retry load data
                        snackBarRetry(seriesList.message)
                    }
                    //Prevent re-shimmer
                    viewModel.setAlreadyShimmer()
                }

                is Resource.Error -> {
                    //Show snackbar for retry load data
                    snackBarRetry(seriesList.message)
                    binding.shimmerLayoutSeries.stopShimmer()
                }
            }
        })
    }

    private fun startShimmer() {
        if (lastBottomLocation == 0) {
            with(binding) {
                shimmerLayoutSeries.startShimmer()
                shimmerLayoutSeries.visibility = View.VISIBLE
            }
        }
    }

    private fun snackBarRetry(message: String?) {
        showSnackBar(requireContext(), bottomNavigationView, message) {
            viewModel.refresh()
        }
    }

    private fun stopShimmer() {
        with(binding) {
            shimmerLayoutSeries.stopShimmer()
            shimmerLayoutSeries.visibility = View.GONE
            tvSeriesPopularTitle.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        moduleSeries.close()
        binding.root.removeAllViewsInLayout()
        _binding = null
    }

}