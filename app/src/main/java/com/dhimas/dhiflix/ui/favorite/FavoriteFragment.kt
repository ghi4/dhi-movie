package com.dhimas.dhiflix.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteMovieAdapter: FavoriteAdapter
    private lateinit var favoriteSeriesAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Garong", "vCreate")
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Garong", "ViewCreated")

        if (activity != null) {
            Log.d("Garong", "NOT NULL")

            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
            viewModel.refresh()

            favoriteMovieAdapter = FavoriteAdapter()
            favoriteSeriesAdapter = FavoriteAdapter()

            viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favoriteMovieList ->
                Log.d("Garong", "VM Movie")
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
                        favoriteMovieAdapter.setMovies(Constant.MOVIE_TYPE, true)
                        favoriteMovieAdapter.notifyDataSetChanged()

                        progressBar2.visibility = View.GONE

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
                        tv_favorite_info.text = favoriteMovieList.message
                    }
                }
            })

            viewModel.getFavoriteSeries().observe(viewLifecycleOwner, { favoriteSeriesList ->
                Log.d("Garong", "VM Series")
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
                        favoriteSeriesAdapter.setMovies(Constant.SERIES_TYPE, true)
                        favoriteSeriesAdapter.notifyDataSetChanged()

                        progressBar2.visibility = View.GONE

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

                        tv_favorite_info.text = favoriteSeriesList.message
                    }
                }
            })
        }

        rv_favorite_movie.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_favorite_movie.hasFixedSize()
        rv_favorite_movie.adapter = favoriteMovieAdapter

        rv_favorite_series.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_favorite_series.hasFixedSize()
        rv_favorite_series.adapter = favoriteSeriesAdapter
    }

    private fun setViewVisibility(loading: Boolean, ivIllustration: Boolean, tvInfo: Boolean) {
        progressBar2.visibility = if (loading) View.VISIBLE else View.GONE
        iv_favorite.visibility = if (ivIllustration) View.VISIBLE else View.GONE
        tv_favorite_info.visibility = if (tvInfo) View.VISIBLE else View.GONE
    }

    private fun setMovieViewVisibility(tvMovie: Boolean, rvMovie: Boolean) {
        tv_movie_title.visibility = if (tvMovie) View.VISIBLE else View.GONE
        rv_favorite_movie.visibility = if (rvMovie) View.VISIBLE else View.GONE
    }

    private fun setSeriesViewVisibility(tvSeries: Boolean, rvSeries: Boolean) {
        tv_series_title.visibility = if (tvSeries) View.VISIBLE else View.GONE
        rv_favorite_series.visibility = if (rvSeries) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()

        setMovieViewVisibility(tvMovie = false, rvMovie = false)
        setSeriesViewVisibility(tvSeries = false, rvSeries = false)
        setViewVisibility(loading = true, ivIllustration = true, tvInfo = true)
        viewModel.refresh()
    }

    override fun onPause() {
        super.onPause()

        Log.d("Garong", "ON Pause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("Garong", "ON Destroy")
    }

    override fun onStart() {
        super.onStart()

        Log.d("Garong", "ON Start")
    }

    override fun onStop() {
        super.onStop()

        Log.d("Garong", "ON Stop")
    }

}