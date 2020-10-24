package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.utils.Utils
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var showId: String
    private lateinit var showType: String
    private lateinit var detailAdapter: DetailAdapter

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

        val factory = ViewModelFactory.getInstance()
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getStringExtra(EXTRA_SHOW_TYPE).toString()

        detailAdapter = DetailAdapter()

        startShimmering()

        if (!viewModel.isAlreadyShimmer) {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
                viewModel.setAlreadyShimmer()
            }, 1000)

            viewModel.getShowList(showType).observe(this, { movieList ->
                detailAdapter.setMovies(movieList as ArrayList<ShowEntity>, showType, false)
                detailAdapter.notifyDataSetChanged()
            })
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModelObserve()
                viewModel.setAlreadyShimmer()
            }, 100)

            viewModel.getShowList(showType).observe(this, { movieList ->
                detailAdapter.setMovies(movieList as ArrayList<ShowEntity>, showType, true)
                detailAdapter.notifyDataSetChanged()
            })
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_other_movie.layoutManager = layoutManager
        rv_other_movie.hasFixedSize()
        rv_other_movie.adapter = detailAdapter
    }

    private fun viewModelObserve() {
        viewModel.getShowEntityById(showId, showType).observe(this, { showEntity ->
            tv_detail_title.text = showEntity.title
            tv_detail_release_year.text = Utils.dateParseToMonthAndYear(showEntity.releaseYear!!)
            tv_detail_overview.text = showEntity.overview

            //For backdrop image
            val backdropTargetWidth = 1280
            val backdropTargetHeight = 720

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + showEntity.backdropPath!!)
                    .placeholder(R.drawable.backdrop_placeholder)
                    .error(R.drawable.image_error)
                    .resize(backdropTargetWidth, backdropTargetHeight)
                    .into(iv_detail_backdrop)

            //For poster image
            val posterTargetWidth = 200
            val posterTargetHeight = 300

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + showEntity.posterPath!!)
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.image_error_2_3)
                    .resize(posterTargetWidth, posterTargetHeight)
                    .into(iv_detail_poster)

            stopShimmering()
        })
    }

    private fun stopShimmering() {
        iv_detail_poster.stopLoading()
        tv_detail_title.stopLoading()
        tv_detail_release_year.stopLoading()
        tv_overview.stopLoading()
        tv_detail_overview.stopLoading()
        tv_interest.stopLoading()
    }

    private fun startShimmering() {
        iv_detail_poster.startLoading()
        tv_detail_title.startLoading()
        tv_detail_release_year.startLoading()
        tv_overview.startLoading()
        tv_detail_overview.startLoading()
        tv_interest.startLoading()
    }
}