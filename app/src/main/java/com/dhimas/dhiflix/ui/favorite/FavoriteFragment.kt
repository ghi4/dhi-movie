package com.dhimas.dhiflix.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.databinding.FragmentFavoriteBinding
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteMovieAdapter: FavoriteAdapter
    private lateinit var favoriteSeriesAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
            //viewModel.refresh()

            setViewVisibility(loading = true, ivIllustration = false, tvInfo = false)

            favoriteMovieAdapter = FavoriteAdapter()
            favoriteSeriesAdapter = FavoriteAdapter()

            viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favoriteMovieList ->
                when (favoriteMovieList.status) {
                    Status.LOADING -> {
                        setViewVisibility(
                            loading = true,
                            ivIllustration = true,
                            tvInfo = true
                        )
                    }
                    Status.SUCCESS -> {
                        favoriteMovieAdapter.submitList(favoriteMovieList.data)
                        favoriteMovieAdapter.setMovies(Const.MOVIE_TYPE, true)
                        favoriteMovieAdapter.notifyDataSetChanged()

                        binding.pbFavorite.visibility = View.GONE

                        if (!favoriteMovieList.data.isNullOrEmpty()) {
                            setMovieViewVisibility(tvMovie = true, rvMovie = true)
                            setViewVisibility(
                                loading = false,
                                ivIllustration = false,
                                tvInfo = false
                            )
                        }
                    }
                    Status.ERROR -> {
                        setViewVisibility(
                            loading = false,
                            ivIllustration = true,
                            tvInfo = true
                        )
                        binding.tvFavoriteInfo.text = favoriteMovieList.message
                    }
                }
            })

            viewModel.getFavoriteSeries().observe(viewLifecycleOwner, { favoriteSeriesList ->
                when (favoriteSeriesList.status) {
                    Status.LOADING -> {
                        setViewVisibility(
                            loading = true,
                            ivIllustration = true,
                            tvInfo = true
                        )
                    }
                    Status.SUCCESS -> {
                        favoriteSeriesAdapter.submitList(favoriteSeriesList.data)
                        favoriteSeriesAdapter.setMovies(Const.SERIES_TYPE, true)
                        favoriteSeriesAdapter.notifyDataSetChanged()

                        binding.pbFavorite.visibility = View.GONE

                        if (!favoriteSeriesList.data.isNullOrEmpty()) {
                            setSeriesViewVisibility(tvSeries = true, rvSeries = true)

                            setViewVisibility(
                                loading = false,
                                ivIllustration = false,
                                tvInfo = false
                            )
                        }
                    }
                    Status.ERROR -> {
                        setViewVisibility(
                            loading = false,
                            ivIllustration = true,
                            tvInfo = true
                        )

                        binding.tvFavoriteInfo.text = favoriteSeriesList.message
                    }
                }
            })
        }

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

    private fun setViewVisibility(loading: Boolean, ivIllustration: Boolean, tvInfo: Boolean) {
        with(binding) {
            pbFavorite.visibility = if (loading) View.VISIBLE else View.GONE
            ivFavoriteInfo.visibility = if (ivIllustration) View.VISIBLE else View.GONE
            tvFavoriteInfo.visibility = if (tvInfo) View.VISIBLE else View.GONE
        }
    }

    private fun setMovieViewVisibility(tvMovie: Boolean, rvMovie: Boolean) {
        with(binding) {
            tvMovieTitle.visibility = if (tvMovie) View.VISIBLE else View.GONE
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
        setViewVisibility(loading = true, ivIllustration = true, tvInfo = true)
        viewModel.refresh()
    }

}