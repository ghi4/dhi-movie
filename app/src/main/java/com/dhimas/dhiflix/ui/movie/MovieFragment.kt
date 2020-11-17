package com.dhimas.dhiflix.ui.movie

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
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sliderAdapter: SliderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
            movieAdapter = MovieAdapter()
            sliderAdapter = SliderAdapter(requireContext())

            //Minimum time for shimmer
            val minShimmerTime = if (!viewModel.isAlreadyShimmer) Constant.MINIMUM_SHIMMER_TIME else 0

            if (viewModel.isAlreadyShimmer) {
                stopShimmer()
            }

            //Delay loading for shimmer
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
            }, minShimmerTime)

            //Change grid layout spanCount when Landscape/Portrait
            val phoneOrientation = requireActivity().resources.configuration.orientation
            val spanCount = if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7
            rv_movie.layoutManager = GridLayoutManager(context, spanCount)
            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
            vp_slider.adapter = sliderAdapter
        }
    }

    private fun viewModelObserve() {
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                if (movieList != null) {
                    when (movieList.status) {
                        Status.LOADING -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            movieAdapter.submitList(movieList.data)
                            movieAdapter.notifyDataSetChanged()

                            if(movieList.data != null) {
                                for (item in movieList.data) {
                                    sliderAdapter.sliderEntities.add(item)
                                }
                                sliderAdapter.notifyDataSetChanged()
                                dots_indicator.setViewPager2(vp_slider)
                                textView3.visibility = View.VISIBLE
                            }

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