package com.dhimas.dhiflix.ui.search.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.search_movie_fragment.*

class SearchMovieFragment : Fragment() {
    private lateinit var movieAdapter: MovieAdapter

    companion object {
        private lateinit var viewModel: SearchViewModel

        fun newInstance(viewModel: SearchViewModel): SearchMovieFragment {
            val fragment = SearchMovieFragment()

            Companion.viewModel = viewModel

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_movie_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter()
        Log.d("Garongx","In VCreated")

        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            Log.d("Garongx","In Observe")
            when (movieList.status) {
                Status.SUCCESS -> {
                    Log.d("Garongx","In Success")
                    movieAdapter.submitList(movieList.data)
                    movieAdapter.notifyDataSetChanged()

                    if (movieList.data != null) {
                        Log.d("Garongx","In Not Null")
                        setViewVisibility(
                            loading = false,
                            rvMovie = true,
                            ivIllustration = false,
                            tvInfo = false
                        )
                        if (movieList.data.isNullOrEmpty()) {
                            Log.d("Garongx","In Null")
                            setViewVisibility(
                                loading = false,
                                rvMovie = false,
                                ivIllustration = true,
                                tvInfo = true
                            )
                            setInfoImageAndMessage(
                                R.drawable.undraw_not_found_60pq,
                                "No movie found."
                            )
                        }
                    } else {
                        Log.d("Garongx","In Null 2")
                        setViewVisibility(
                            loading = false,
                            rvMovie = false,
                            ivIllustration = true,
                            tvInfo = true
                        )
                        setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, "No movie found.")
                    }
                }

                Status.LOADING -> {
                    Log.d("Garongx","In Loading")
                    setViewVisibility(
                        loading = true,
                        rvMovie = true,
                        ivIllustration = false,
                        tvInfo = false
                    )
                }

                Status.ERROR -> {
                    Log.d("Garongx","In Error")
                    setViewVisibility(
                        loading = false,
                        rvMovie = false,
                        ivIllustration = true,
                        tvInfo = true
                    )
                    setInfoImageAndMessage(
                        R.drawable.undraw_not_found_60pq,
                        movieList.message.toString()
                    )
                }
            }
        })

        rv_search_movies.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_movies.hasFixedSize()
        rv_search_movies.adapter = movieAdapter
    }

    private fun setViewVisibility(
        loading: Boolean,
        rvMovie: Boolean,
        ivIllustration: Boolean,
        tvInfo: Boolean
    ) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        rv_search_movies.visibility = if (rvMovie) View.VISIBLE else View.INVISIBLE
        iv_movie_illustration.visibility = if (ivIllustration) View.VISIBLE else View.INVISIBLE
        tv_movie_info.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
    }

    private fun setInfoImageAndMessage(image: Int, message: String) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(iv_movie_illustration)
        tv_movie_info.text = message
    }

    override fun onResume() {
        super.onResume()

        Log.d("Garongx", "ON Resume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("Garongx", "ON Pause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("Garongx", "ON Destroy")
    }

    override fun onStart() {
        super.onStart()

        Log.d("Garongx", "ON Start")
    }

    override fun onStop() {
        super.onStop()

        Log.d("Garongx", "ON Stop")
    }

}