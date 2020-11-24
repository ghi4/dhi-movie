package com.dhimas.dhiflix.ui.search.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.FragmentSearchMovieBinding
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso

class SearchMovieFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels({ requireParentFragment() })
    private lateinit var binding: FragmentSearchMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            when (movieList.status) {
                Status.SUCCESS -> {
                    movieAdapter.addMovies(movieList.data as ArrayList<ShowEntity>)
                    movieAdapter.notifyDataSetChanged()

                    if (!movieList.data.isNullOrEmpty()) {
                        setViewVisibility(
                            loading = false,
                            rvMovie = true,
                            ivIllustration = false,
                            tvInfo = false
                        )
                    } else {
                        setViewVisibility(
                            loading = false,
                            rvMovie = false,
                            ivIllustration = true,
                            tvInfo = true
                        )
                        setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, getString(R.string.no_movie_found))
                    }
                }

                Status.LOADING -> {
                    setViewVisibility(
                        loading = true,
                        rvMovie = true,
                        ivIllustration = false,
                        tvInfo = false
                    )
                }

                Status.ERROR -> {
                    setViewVisibility(
                        loading = false,
                        rvMovie = false,
                        ivIllustration = true,
                        tvInfo = true
                    )
                    setInfoImageAndMessage(
                        R.drawable.undraw_signal_searching_bhpc,
                        movieList.message ?: getString(R.string.unknown_error)
                    )
                }
            }
        })
    }

    private fun setupUI() {
        movieAdapter = MovieAdapter()

        with(binding) {
            rvSearchMovie.layoutManager = GridLayoutManager(requireContext(), 3)
            rvSearchMovie.hasFixedSize()
            rvSearchMovie.adapter = movieAdapter
        }
    }

    private fun setViewVisibility(
        loading: Boolean,
        rvMovie: Boolean,
        ivIllustration: Boolean,
        tvInfo: Boolean
    ) {
        with(binding) {
            pbSearchMovie.visibility = if (loading) View.VISIBLE else View.GONE
            rvSearchMovie.visibility = if (rvMovie) View.VISIBLE else View.VISIBLE
            ivSearchMovieInfo.visibility = if (ivIllustration) View.VISIBLE else View.INVISIBLE
            tvSearchMovieInfo.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setInfoImageAndMessage(image: Int, message: String) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(binding.ivSearchMovieInfo)
        binding.tvSearchMovieInfo.text = message
    }

}