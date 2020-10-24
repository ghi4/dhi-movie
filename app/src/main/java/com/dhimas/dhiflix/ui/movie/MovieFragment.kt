package com.dhimas.dhiflix.ui.movie

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
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

        if (activity != null && view != null) {
            val factory = ViewModelFactory.getInstance()
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
            movieAdapter = MovieAdapter()

            EspressoIdlingResource.increment()

            //Minimum shimmer time
            //If data loaded too fast causing awkward animation/view
            val shimmerTime = 1000L
            if (!viewModel.isAlreadyShimmer) {
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModelObserve()
                    viewModel.setAlreadyShimmer()
                }, shimmerTime)
            } else {
                movieShimmerLayout.stopShimmer()
                movieShimmerLayout.visibility = View.GONE
                viewModelObserve()
            }

            val phoneOrientation = requireActivity().resources.configuration.orientation
            if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) {
                rv_movie.layoutManager = GridLayoutManager(context, 3)
            } else {
                rv_movie.layoutManager = GridLayoutManager(context, 7)
            }

            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
        }
    }

    private fun viewModelObserve() {
        if (view != null) {
            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                movieAdapter.setMovies(movieList as ArrayList<ShowEntity>)
                movieShimmerLayout.stopShimmer()
                movieShimmerLayout.visibility = View.GONE
                movieAdapter.notifyDataSetChanged()

                EspressoIdlingResource.decrement()
            })
        }
    }
}