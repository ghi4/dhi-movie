package com.dhimas.dhiflix.ui.movie

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

        var alreadyShimmer = false

        if (activity != null) {
            val factory = ViewModelFactory.getInstance()
            viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]
            movieAdapter = MovieAdapter()

            if (!alreadyShimmer) {
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModelObserve()
                    alreadyShimmer = true
                }, 1000)
            } else {
                movieShimmerLayout.stopShimmer()
                movieShimmerLayout.visibility = View.GONE
                viewModelObserve()
            }

            rv_movie.layoutManager = GridLayoutManager(context, 3)
            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
        }
    }

    private fun viewModelObserve() {
        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            movieAdapter.setMovies(movieList as ArrayList<ShowEntity>)
            movieShimmerLayout.stopShimmer()
            movieShimmerLayout.visibility = View.GONE
            movieAdapter.notifyDataSetChanged()
        })
    }
}