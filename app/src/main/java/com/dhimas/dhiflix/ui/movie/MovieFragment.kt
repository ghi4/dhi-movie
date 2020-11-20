package com.dhimas.dhiflix.ui.movie

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.SliderAdapter
import com.dhimas.dhiflix.utils.Utils.doneDelay
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.utils.Utils.waitDelay
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private var page = 1
    private var isBottom = false
    private var scrollLocation = 0
    private var stateScroll: Parcelable? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        movieAdapter = MovieAdapter()
        sliderAdapter = SliderAdapter(requireContext())
        viewModel.setPage(page)

        setupUI()

        //Delay for shimmer animation
        waitDelay()
        val minShimmerTime = getMinShimmerTime(viewModel.isAlreadyShimmer)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserver()
            doneDelay()
        }, minShimmerTime)

    }

    private fun setupUI() {
        //Prevent re-shimmer
        if (viewModel.isAlreadyShimmer)
            stopShimmer()

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
        rv_movie.layoutManager = GridLayoutManager(context, spanCount)
        rv_movie.hasFixedSize()
        rv_movie.adapter = movieAdapter
        rv_movie.isNestedScrollingEnabled = false

        vp_slider.adapter = sliderAdapter
        dots_indicator.setViewPager2(vp_slider)

        nestedScrollMovie.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (scrollY == (v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight ?: 0) && !isBottom) {
                    page++
                    viewModel.setPage(page)

                    isBottom = true
                    scrollLocation = scrollY
                    showToast(requireContext(), "Load more.")

                    stateScroll = rv_movie.layoutManager?.onSaveInstanceState()
                }
            })


        movieAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onChanged() {
                rv_movie.layoutManager?.onRestoreInstanceState(stateScroll)
                nestedScrollMovie.scrollTo(0, scrollLocation )
                rv_movie.scrollToPosition(20 * (page - 1))
            }
        })
    }

    private fun viewModelObserver() {
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                when (movieList.status) {
                    Status.LOADING -> {

                    }

                    Status.SUCCESS -> {
                        if (movieList.data != null) {
                            movieAdapter.submitList(movieList.data)

                            sliderAdapter.sliderEntities.clear()
                            for (i in 0..4)
                                movieList.data[i]?.let { sliderAdapter.sliderEntities.add(it) }
                            sliderAdapter.notifyDataSetChanged()

                            textView3.visibility = View.VISIBLE
                            stopShimmer()
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

    private fun abc(runnable: Runnable){
        Handler(Looper.getMainLooper()).postDelayed(runnable, 500L)
    }

    private fun startShimmer() {
        movieShimmerLayout.startShimmer()
        movieShimmerLayout.visibility = View.VISIBLE
    }

    private fun stopShimmer() {
        movieShimmerLayout.stopShimmer()
        movieShimmerLayout.visibility = View.GONE
    }
}