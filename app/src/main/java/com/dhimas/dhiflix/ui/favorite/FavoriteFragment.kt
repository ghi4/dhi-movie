package com.dhimas.dhiflix.ui.favorite

import android.os.Bundle
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
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
            favoriteMovieAdapter = FavoriteAdapter()
            favoriteSeriesAdapter = FavoriteAdapter()

            viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favoriteMovieList ->
                if (favoriteMovieList != null) {
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
                                rv_favorite_movie.visibility = View.VISIBLE
                                textView.visibility = View.VISIBLE

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
                }
            })

            viewModel.getFavoriteSeries().observe(viewLifecycleOwner, { favoriteSeriesList ->
                if (favoriteSeriesList != null) {
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
                                rv_favorite_series.visibility = View.VISIBLE
                                textView2.visibility = View.VISIBLE

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

    private fun setViewVisibility(loading: Boolean, ivIllustration: Boolean, tvInfo: Boolean){
        progressBar2.visibility = if(loading) View.VISIBLE else View.GONE
        iv_favorite.visibility = if(ivIllustration) View.VISIBLE else View.GONE
        tv_favorite_info.visibility = if(tvInfo) View.VISIBLE else View.GONE
    }

}