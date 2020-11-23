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
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.FragmentMovieBinding
import com.dhimas.dhiflix.ui.SliderAdapter
import com.dhimas.dhiflix.utils.Utils.doneDelay
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.utils.Utils.waitDelay
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status

class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private var page = 1
    private var scrollLocation = 0

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
        viewModel = ViewModelProvider(requireActivity(), factory)[MovieViewModel::class.java]
        movieAdapter = MovieAdapter()
        sliderAdapter = SliderAdapter(requireContext())
        viewModel.setPage(page)

        stopShimmer()

        //Delay for shimmer animation
        waitDelay()
        val minShimmerTime = getMinShimmerTime(viewModel.getIsAlreadyShimmer())
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserver()
        }, minShimmerTime)

        setupUI()
    }

    private fun setupUI() {
        //Prevent re-shimmer
        if (viewModel.getIsAlreadyShimmer())
            stopShimmer()

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
        with(binding) {
            rvMovie.layoutManager = GridLayoutManager(context, spanCount)
            rvMovie.hasFixedSize()
            rvMovie.adapter = movieAdapter
            rvMovie.isNestedScrollingEnabled = false

            vpSlider.adapter = sliderAdapter
            dotsIndicator.setViewPager2(vpSlider)

            nestedScrollMovie.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
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
                })
        }
    }

    private fun viewModelObserver() {
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                when (movieList.status) {
                    Status.LOADING -> {
                        if (scrollLocation == 0 && !viewModel.getIsAlreadyShimmer())
                            startShimmer()
                    }

                    Status.SUCCESS -> {
                        if (movieList.data != null) {
                            movieAdapter.addMovies(movieList.data as ArrayList<ShowEntity>)
                            movieAdapter.notifyDataSetChanged()

                            sliderAdapter.sliderEntities.clear()
                            for (i in 0..4)
                                movieList.data[i].let { sliderAdapter.sliderEntities.add(it) }
                            sliderAdapter.notifyDataSetChanged()

                            binding.textView3.visibility = View.VISIBLE
                            stopShimmer()
                            if (!viewModel.getIsAlreadyShimmer())
                                doneDelay()
                        } else {
                            showToast(requireContext(), "No movie found.")
                            showSnackBar(requireView(), "Do you want to retry?") {
                                viewModel.refresh()
                            }
                        }
                        viewModel.setAlreadyShimmer()
                    }

                    Status.ERROR -> {
                        showSnackBar(requireView(), movieList.message.toString()) {
                            viewModel.refresh()
                        }
                    }
                }
            })
        }
    }

    private fun startShimmer() {
        with(binding) {
            movieShimmerLayout.startShimmer()
            movieShimmerLayout.visibility = View.VISIBLE
        }
    }

    private fun stopShimmer() {
        with(binding) {
            movieShimmerLayout.stopShimmer()
            movieShimmerLayout.visibility = View.GONE
        }
    }
}