package com.dhimas.dhiflix.ui.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
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
            val factory = ViewModelFactory.getInstance()
            val viewModel = ViewModelProvider(this, factory)[MovieViewModel::class.java]

            val movieAdapter = MovieAdapter()

            viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
                movieAdapter.setMovies(movieList as ArrayList<ShowEntity>)
                movieAdapter.notifyDataSetChanged()
            })

            rv_movie.layoutManager = GridLayoutManager(context, 3)
            rv_movie.hasFixedSize()
            rv_movie.adapter = movieAdapter
        }
    }
}