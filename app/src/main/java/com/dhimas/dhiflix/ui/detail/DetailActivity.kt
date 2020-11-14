package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.Utils
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var showId: String
    private lateinit var showType: String
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var showEntity1: ShowEntity

    companion object {
        //For sending Show Title
        const val EXTRA_SHOW_ID = "extra_show_id"

        //FOr sending Show Type (Movie/Series)
        const val EXTRA_SHOW_TYPE = "extra_show_type"

        //For checking if Show Title is from Movie Fragment
        const val EXTRA_FROM_MOVIES = "extra_from_movies"

        //For checking if Show Title is from Series Fragment
        const val EXTRA_FROM_SERIES = "extra_from_series"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        detailAdapter = DetailAdapter()

        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getStringExtra(EXTRA_SHOW_TYPE).toString()

        val minShimmerTime = if (!viewModel.isAlreadyShimmer) Constant.MINIMUM_SHIMMER_TIME else 100

        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserve()
            viewModel.setAlreadyShimmer()
        }, minShimmerTime)

        viewModel.getShowList(showType, showId).observe(this, { movieList ->
            if (Status.SUCCESS == movieList.status) {
                detailAdapter.setMovies(
                    showType,
                    viewModel.isAlreadyShimmer
                )
                detailAdapter.submitList(movieList.data)
                detailAdapter.notifyDataSetChanged()
            }
        })

        bt_favorite.setOnClickListener {
            viewModel.setFavorite(showEntity1)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_other_movie.layoutManager = layoutManager
        rv_other_movie.hasFixedSize()
        rv_other_movie.adapter = detailAdapter
    }

    private fun viewModelObserve() {
        viewModel.getShowEntityById(showId, showType).observe(this, { showEntity ->
            if (showEntity != null) {
                when (showEntity.status) {
                    Status.LOADING -> startShimmering()
                    Status.SUCCESS -> {
                        if (showEntity.data != null) {

                            this.showEntity1 = showEntity.data

                            tv_detail_title.text = showEntity.data.title
                            tv_detail_release_date.text =
                                Utils.dateParseToMonthAndYear(showEntity.data.releaseDate)
                            tv_detail_overview.text = showEntity.data.overview

                            bt_favorite.text =
                                if (showEntity.data.isFavorite == 0)
                                    getString(R.string.add_to_favorite)
                                else
                                    getString(R.string.remove_from_favorite)

                            Picasso.get()
                                .load(Constant.URL_BASE_IMAGE + showEntity.data.backdropPath)
                                .placeholder(R.drawable.backdrop_placeholder)
                                .error(R.drawable.image_error)
                                .resize(
                                    Constant.BACKDROP_TARGET_WIDTH,
                                    Constant.BACKDROP_TARGET_HEIGHT
                                )
                                .into(iv_detail_backdrop)

                            Picasso.get()
                                .load(Constant.URL_BASE_IMAGE + showEntity.data.posterPath)
                                .placeholder(R.drawable.poster_placeholder)
                                .error(R.drawable.image_error_2_3)
                                .resize(
                                    Constant.POSTER_TARGET_WIDTH,
                                    Constant.POSTER_TARGET_HEIGHT
                                )
                                .into(iv_detail_poster)

                            stopShimmering()
                        }
                    }
                    Status.ERROR -> Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun stopShimmering() {
        iv_detail_poster.stopLoading()
        tv_detail_title.stopLoading()
        tv_detail_release_date.stopLoading()
        tv_overview.stopLoading()
        tv_detail_overview.stopLoading()
        tv_interest.stopLoading()
        bt_favorite.stopLoading()
    }

    private fun startShimmering() {
        iv_detail_poster.startLoading()
        tv_detail_title.startLoading()
        tv_detail_release_date.startLoading()
        tv_overview.startLoading()
        tv_detail_overview.startLoading()
        tv_interest.startLoading()
        bt_favorite.startLoading()
    }
}