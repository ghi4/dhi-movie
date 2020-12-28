package com.dhimas.dhiflix.ui.movie

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
import com.dhimas.dhiflix.databinding.FragmentMovieBinding
import com.dhimas.dhiflix.ui.BannerAdapter
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var movieAdapter: ShowsAdapter
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: MovieViewModel by viewModel()
    private var currentPage = 1
    private var maxPage = 6
    private var lastBottomLocation = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
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
        movieAdapter = ShowsAdapter()

        //OnClick go to DetailActivity
        movieAdapter.onItemClick = { selectedShow ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7

        with(binding) {
            rvMovie.layoutManager = GridLayoutManager(context, spanCount)
            rvMovie.hasFixedSize()
            rvMovie.adapter = movieAdapter
            rvMovie.isNestedScrollingEnabled = false

            vpMovieBanner.adapter = bannerAdapter
            dotsIndicatorMovie.setViewPager2(vpMovieBanner)

            //Trigger "Load More" when at bottom and prevent double load request
            nestedScrollMovie.setOnScrollChangeListener(
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
        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            when (movieList) {
                is Resource.Loading -> {
                    if (lastBottomLocation == 0)
                        startShimmer()
                }

                is Resource.Success -> {
                    if (movieList.data != null) {
                        movieAdapter.setList(movieList.data as ArrayList<Show>)

                        bannerAdapter.clearBanner()
                        for (i in 0..4)
                            (movieList.data as ArrayList<Show>)[i].let { bannerAdapter.addBanner(it) }
                        bannerAdapter.notifyDataSetChanged()

                        stopShimmer()
                    } else {
                        showToast(requireContext(), getString(R.string.no_movie_found))
                        //Show snackbar for retry load data
                        showSnackBar(
                            requireView(),
                            getString(R.string.do_you_want_retry),
                            getString(R.string.retry)
                        ) {
                            viewModel.refresh()
                        }
                    }
                    viewModel.setAlreadyShimmer()
                }

                is Resource.Error -> {
                    //Show snackbar for retry load data
                    showSnackBar(
                        bottomNavigationView,
                        movieList.message ?: getString(R.string.unknown_error),
                        getString(R.string.retry)
                    ) {
                        viewModel.refresh()
                    }
                }
            }
        })
    }

    private fun startShimmer() {
        with(binding) {
            shimmerLayoutMovie.startShimmer()
            shimmerLayoutMovie.visibility = View.VISIBLE
        }
    }

    private fun stopShimmer() {
        with(binding) {
            shimmerLayoutMovie.stopShimmer()
            shimmerLayoutMovie.visibility = View.GONE
            tvMoviePopularTitle.visibility = View.VISIBLE
        }
    }
}