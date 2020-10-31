package com.dhimas.dhiflix.ui.favorite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.ui.detail.DetailAdapter
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.series.SeriesAdapter
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import kotlinx.android.synthetic.main.fragment_favorite.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteMovieAdapter: DetailAdapter
    private lateinit var favoriteSeriesAdapter: DetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(activity != null && view != null && context != null){

            val factory = ViewModelFactory.getInstance(requireContext())
            viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

            favoriteMovieAdapter = DetailAdapter()
            favoriteSeriesAdapter = DetailAdapter()

            viewModel.getFavoriteMovies().observe(viewLifecycleOwner, {favoriteMovieList ->
                if(favoriteMovieList != null){
                    when(favoriteMovieList.status){
                        Status.LOADING -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            Log.d("Kucing", "${favoriteMovieList.data?.size}")
                            favoriteMovieAdapter.setMovies(favoriteMovieList.data as ArrayList<ShowEntity>, DetailActivity.EXTRA_FROM_MOVIES, true)
                            rv_favorite_movie.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            rv_favorite_movie.hasFixedSize()
                            favoriteMovieAdapter.notifyDataSetChanged()
                            rv_favorite_movie.adapter = favoriteMovieAdapter
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })

            viewModel.getFavoriteSeries().observe(viewLifecycleOwner, {favoriteSeriesList ->
                if(favoriteSeriesList != null){
                    when(favoriteSeriesList.status){
                        Status.LOADING -> {
                            Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                        }
                        Status.SUCCESS -> {
                            Log.d("Kucing", "${favoriteSeriesList.data?.size}")
                            favoriteSeriesAdapter.setMovies(favoriteSeriesList.data as ArrayList<ShowEntity>, DetailActivity.EXTRA_FROM_SERIES, true)
                            rv_favorite_series.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            rv_favorite_series.hasFixedSize()
                            favoriteSeriesAdapter.notifyDataSetChanged()
                            rv_favorite_series.adapter = favoriteSeriesAdapter
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

}