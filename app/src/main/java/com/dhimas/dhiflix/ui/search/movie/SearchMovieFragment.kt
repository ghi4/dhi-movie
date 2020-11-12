package com.dhimas.dhiflix.ui.search.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.search_movie_fragment.*

class SearchMovieFragment : Fragment() {

    private lateinit var viewModel: SearchMovieViewModel

    companion object {
        private var keyword: String? = null

        fun newInstance(keyword: String?): SearchMovieFragment {
            val fragment = SearchMovieFragment()
            Companion.keyword = keyword

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory).get(SearchMovieViewModel::class.java)

        val movieAdapter = MovieAdapter()

        keyword?.let {
            viewModel.searchMovie(it).observe(viewLifecycleOwner, { movieList ->
                if (!movieList.data.isNullOrEmpty()) {
                    movieAdapter.submitList(movieList.data)
                    movieAdapter.notifyDataSetChanged()
                }
            })
        }

        rv_search_movies.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_movies.hasFixedSize()
        rv_search_movies.adapter = movieAdapter
    }

}