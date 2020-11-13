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
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
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
                when (movieList.status) {
                    Status.SUCCESS -> {
                        movieAdapter.submitList(movieList.data)
                        movieAdapter.notifyDataSetChanged()

                        progressBar.visibility = View.GONE
                        iv_movie_illustration.visibility = View.GONE
                        tv_movie_info.visibility = View.GONE
                    }

                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        iv_movie_illustration.visibility = View.GONE
                        tv_movie_info.visibility = View.GONE
                    }

                    Status.ERROR -> {
                        progressBar.visibility = View.GONE

                        val imgSize = 230

                        Picasso.get()
                            .load(R.drawable.undraw_not_found_60pq)
                            .placeholder(R.drawable.backdrop_placeholder)
                            .error(R.drawable.image_error)
                            .resize(imgSize, imgSize)
                            .into(iv_movie_illustration)
                        tv_movie_info.text = movieList.message

                        iv_movie_illustration.visibility = View.VISIBLE
                        tv_movie_info.visibility = View.VISIBLE

                    }
                }
            })
        }

        rv_search_movies.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_movies.hasFixedSize()
        rv_search_movies.adapter = movieAdapter
    }

}