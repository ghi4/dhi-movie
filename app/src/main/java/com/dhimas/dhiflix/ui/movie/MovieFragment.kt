package com.dhimas.dhiflix.ui.movie

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.FragmentMovieBinding
import com.dhimas.dhiflix.ui.SliderAdapter
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
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

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        viewModel.setPage(currentPage)

        bottomNavigationView = requireActivity().findViewById(R.id.nav_view)

        setupUI()

        //Delay for shimmer animation
        val minShimmerTime = getMinShimmerTime(viewModel.getIsAlreadyShimmer())
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserver()
        }, minShimmerTime)

    }

    private fun setupUI() {
        //Prevent re-shimmer
        if (viewModel.getIsAlreadyShimmer())
            stopShimmer()
        else
            startShimmer()

        movieAdapter = MovieAdapter()
        sliderAdapter = SliderAdapter(requireContext())

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
        with(binding) {
            rvMovie.layoutManager = GridLayoutManager(context, spanCount)
            rvMovie.hasFixedSize()
            rvMovie.adapter = movieAdapter
            rvMovie.isNestedScrollingEnabled = false

            vpMovieBanner.adapter = sliderAdapter
            dotsIndicatorMovie.setViewPager2(vpMovieBanner)

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
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                when (movieList.status) {
                    Status.LOADING -> {
                        if (lastBottomLocation == 0)
                            startShimmer()
                    }

                    Status.SUCCESS -> {
                        if (movieList.data != null) {
                            movieAdapter.addMovies(movieList.data as ArrayList<ShowEntity>)
                            movieAdapter.notifyDataSetChanged()

                            sliderAdapter.clearBanner()
                            for (i in 0..4)
                                movieList.data[i].let { sliderAdapter.addBanner(it) }
                            sliderAdapter.notifyDataSetChanged()

                            stopShimmer()
                        } else {
                            showToast(requireContext(), getString(R.string.no_movie_found))
                            showSnackBar(requireView(), getString(R.string.do_you_want_retry)) {
                                viewModel.refresh()
                            }
                        }
                        viewModel.setAlreadyShimmer()
                    }

                    Status.ERROR -> {
                        showSnackBar(bottomNavigationView, movieList.message ?: getString(R.string.unknown_error)) {
                            viewModel.refresh()
                        }
                    }
                }
            })
        }
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