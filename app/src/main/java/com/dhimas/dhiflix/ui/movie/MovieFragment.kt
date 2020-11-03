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
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment() {
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null && view != null && context != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
            movieAdapter = MovieAdapter()

            val minShimmerTime = if(!viewModel.isAlreadyShimmer) Constant.MINIMUM_SHIMMER_TIME else 0

            //Prevent re-shimmer when rotating phone
            if(viewModel.isAlreadyShimmer){
                stopShimmer()
            }

            //Delay loading for shimmer
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
                viewModel.setAlreadyShimmer()
            }, minShimmerTime)

            //Change grid layout spanCount when Landscape/Portrait
            val phoneOrientation = requireActivity().resources.configuration.orientation
            val spanCount = if(phoneOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 7

            rv_movie.layoutManager = GridLayoutManager(context, spanCount)
            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
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
                            movieAdapter.setMovies(movieList.data as ArrayList<ShowEntity>)
                            movieAdapter.notifyDataSetChanged()
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                stopShimmer()
            })
        }
    }

    private fun stopShimmer(){
        movieShimmerLayout.stopShimmer()
        movieShimmerLayout.visibility = View.GONE
    }
}