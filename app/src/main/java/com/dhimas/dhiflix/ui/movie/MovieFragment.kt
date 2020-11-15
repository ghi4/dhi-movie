package com.dhimas.dhiflix.ui.movie

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*
import kotlinx.android.synthetic.main.fragment_movie.view.*

class MovieFragment : Fragment() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
        movieAdapter = MovieAdapter()
        sliderAdapter = SliderAdapter(requireActivity())

        val root = inflater.inflate(R.layout.fragment_movie, container, false)
        root.movieShimmerLayout.startShimmer()

        //Prevent re-shimmer when rotating phone
        if (viewModel.isAlreadyShimmer) {
            root.movieShimmerLayout.stopShimmer()
            root.movieShimmerLayout.visibility = View.GONE
        }

        //Change grid layout spanCount when Landscape/Portrait
        val phoneOrientation = requireActivity().resources.configuration.orientation
        val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7

        root.rv_movie.layoutManager = GridLayoutManager(context, spanCount)
        root.rv_movie.hasFixedSize()
        root.rv_movie.adapter = movieAdapter
        root.vp_slider.adapter = sliderAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("GGWPX", "VIEW CREATED")
        if (activity != null && context != null) {
            val minShimmerTime =
                if (!viewModel.isAlreadyShimmer) Constant.MINIMUM_SHIMMER_TIME else 0

            //Delay loading for shimmer
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
            }, minShimmerTime)
        }
    }

    private fun viewModelObserve() {
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                if (movieList != null) {
                    when (movieList.status) {
                        Status.LOADING -> Toast.makeText(context, "Loading", Toast.LENGTH_SHORT)
                            .show()
                        Status.SUCCESS -> {
                            movieAdapter.submitList(movieList.data)
                            movieAdapter.notifyDataSetChanged()

                            Log.d("GGWPX", "OUT")
                            if(movieList.data != null) {
                                Log.d("GGWPX", "HERE")
                                sliderAdapter.deleteShow()
                                for (item in movieList.data) {
                                    Log.d("GGWPX", "count")
                                    sliderAdapter.addShow(item)
                                }
                            }

                            dots_indicator.setViewPager2(vp_slider)

                            viewModel.setAlreadyShimmer()
                            stopShimmer()
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            showSnackBar()
                        }
                    }
                }
            })
        }
    }

    private fun showSnackBar() {
        Snackbar.make(requireView(), "No internet connection!", Snackbar.LENGTH_INDEFINITE)
            .setAction("RETRY") {
                viewModel.refresh()
                movieAdapter.notifyDataSetChanged()
            }
            .show()
    }

    private fun stopShimmer() {
        movieShimmerLayout.stopShimmer()
        movieShimmerLayout.visibility = View.GONE
    }
}