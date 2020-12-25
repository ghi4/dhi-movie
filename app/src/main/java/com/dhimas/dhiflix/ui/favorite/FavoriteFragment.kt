package com.dhimas.dhiflix.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.databinding.FragmentFavoriteBinding
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteMovieAdapter: MovieAdapter
    private lateinit var favoriteSeriesAdapter: SeriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        setupUI()
        viewModelObserveMovies()
        viewModelObserveSeries()
    }

    private fun setupUI() {
        setViewVisibility(loading = true, ivInfo = false, tvInfo = false)

        favoriteMovieAdapter = MovieAdapter()
        favoriteSeriesAdapter = SeriesAdapter()

        with(binding) {
            rvFavoriteMovie.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvFavoriteMovie.hasFixedSize()
            rvFavoriteMovie.adapter = favoriteMovieAdapter

            rvFavoriteSeries.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvFavoriteSeries.hasFixedSize()
            rvFavoriteSeries.adapter = favoriteSeriesAdapter
        }
    }

    private fun viewModelObserveMovies() {
        viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favoriteMovieList ->
            when (favoriteMovieList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = true, tvInfo = true)
                }
                is Resource.Success -> {
                    favoriteMovieAdapter.addMovies(favoriteMovieList.data as ArrayList<Show>)
                    favoriteMovieAdapter.notifyDataSetChanged()

                    binding.pbFavorite.visibility = View.GONE

                    if (!favoriteMovieList.data.isNullOrEmpty()) {
                        setMovieViewVisibility(tvMovie = true, rvMovie = true)
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }
                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    binding.tvFavoriteInfo.text = favoriteMovieList.message
                }
            }
        })
    }

    private fun viewModelObserveSeries() {
        viewModel.getFavoriteSeries().observe(viewLifecycleOwner, { favoriteSeriesList ->
            when (favoriteSeriesList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = true, tvInfo = true)
                }
                is Resource.Success -> {
                    favoriteSeriesAdapter.addSeries(favoriteSeriesList.data as ArrayList<Show>)
                    favoriteSeriesAdapter.notifyDataSetChanged()

                    binding.pbFavorite.visibility = View.GONE

                    if (!favoriteSeriesList.data.isNullOrEmpty()) {
                        setSeriesViewVisibility(tvSeries = true, rvSeries = true)
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }
                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    binding.tvFavoriteInfo.text = favoriteSeriesList.message
                }
            }
        })
    }

    private fun setViewVisibility(loading: Boolean, ivInfo: Boolean, tvInfo: Boolean) {
        with(binding) {
            pbFavorite.visibility = if (loading) View.VISIBLE else View.GONE
            ivFavoriteInfo.visibility = if (ivInfo) View.VISIBLE else View.GONE
            tvFavoriteInfo.visibility = if (tvInfo) View.VISIBLE else View.GONE
        }
    }

    private fun setMovieViewVisibility(tvMovie: Boolean, rvMovie: Boolean) {
        with(binding) {
            tvFavoriteMovieTitle.visibility = if (tvMovie) View.VISIBLE else View.GONE
            rvFavoriteMovie.visibility = if (rvMovie) View.VISIBLE else View.GONE
        }
    }

    private fun setSeriesViewVisibility(tvSeries: Boolean, rvSeries: Boolean) {
        with(binding) {
            tvFavoriteSeriesTitle.visibility = if (tvSeries) View.VISIBLE else View.GONE
            rvFavoriteSeries.visibility = if (rvSeries) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        setMovieViewVisibility(tvMovie = false, rvMovie = false)
        setSeriesViewVisibility(tvSeries = false, rvSeries = false)
        setViewVisibility(loading = true, ivInfo = false, tvInfo = false)
        viewModel.refresh()
    }

}