package com.dhimas.dhiflix.ui.search.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.databinding.FragmentSearchMovieBinding
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchMovieFragment : Fragment() {
    private val viewModel: SearchViewModel by sharedViewModel()
    private lateinit var binding: FragmentSearchMovieBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            when (movieList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = false, tvInfo = false)
                }

                is Resource.Success -> {
                    movieAdapter.addMovies(movieList.data as ArrayList<Show>)
                    movieAdapter.notifyDataSetChanged()

                    if (!movieList.data.isNullOrEmpty()) {
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    } else {
                        setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                        setInfoImageAndMessage(
                            R.drawable.undraw_not_found_60pq,
                            getString(R.string.no_movie_found)
                        )
                    }
                }

                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
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
        ivInfo: Boolean,
        tvInfo: Boolean
    ) {
        with(binding) {
            pbSearchMovie.visibility = if (loading) View.VISIBLE else View.GONE
            ivSearchMovieInfo.visibility = if (ivInfo) View.VISIBLE else View.INVISIBLE
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