package com.dhimas.dhiflix.ui.movie

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    //private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieAdapterz: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter
    private var page = 1
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
        //movieAdapter = MovieAdapter()
        movieAdapterz = MovieAdapter()
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
        rv_movie.adapter = movieAdapterz
        rv_movie.isNestedScrollingEnabled = false

        vp_slider.adapter = sliderAdapter
        dots_indicator.setViewPager2(vp_slider)

        var savedS = false

        nestedScrollMovie.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                val height = (v?.getChildAt(0)?.measuredHeight ?: 0) - (v?.measuredHeight ?: 0)
                Log.d("GGA", "$scrollY == ${v?.getChildAt(0)?.measuredHeight} == ${v?.measuredHeight} == $height")
                if (scrollY == height && scrollY > scrollLocation) {
                    if(page < 5){
                        page++
                        viewModel.setPage(page)
                        scrollLocation = scrollY
                        showToast(requireContext(), "Load more.")
                        Log.d("GGX", "PAGE: $page")
                        Log.d("GGA", "=========================")
                        savedS = false
                    } else {
                        savedS = true
                        showToast(requireContext(), "Max page.")
                    }
                }

//                if(!savedS && scrollY < scrollLocation){
//                    Log.d("GGA", "GO GO GO")
//                    nestedScrollMovie.scrollTo(0, scrollLocation)
//                    if(scrollY == scrollLocation) {
//                        savedS = true
//                        Log.d("GGA", "GO GO SAVED")
//                    }
//                }
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
                            //movieAdapter.submitList(movieList.data)
                            movieAdapterz.addMovie(movieList.data as ArrayList<ShowEntity>)
                            movieAdapterz.notifyDataSetChanged()

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