package com.dhimas.dhiflix.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import kotlinx.android.synthetic.main.fragment_home.*

class MovieFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null) {
            val viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
            val movies = viewModel.getMovies()

            val movieAdapter = MovieAdapter()
            movieAdapter.setMovies(movies)

            rv_movie.layoutManager = GridLayoutManager(context, 3)
            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
        }
    }
}