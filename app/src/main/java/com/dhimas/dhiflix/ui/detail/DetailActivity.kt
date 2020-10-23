package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

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
        val viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
        val showId = intent.getStringExtra(EXTRA_SHOW_ID)
        val showType = intent.getStringExtra(EXTRA_SHOW_TYPE)

        if (showId != null && showType != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                viewModel.getShowEntityById(showId, showType).observe(this, { showEntity ->
                    tv_detail_title.text = showEntity.title
                    tv_detail_release_year.text = showEntity.releaseYear
                    tv_detail_overview.text = showEntity.overview

                    //For backdrop image
                    val backdropTargetWidth = 800
                    val backdropTargetHeight = 450

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
            }, 1000)

            val detailAdapter = DetailAdapter()

            viewModel.getShowList(showType).observe(this, { movieList ->
                detailAdapter.setMovies(movieList as ArrayList<ShowEntity>, showType)
                detailAdapter.notifyDataSetChanged()
            })

            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_other_movie.layoutManager = layoutManager
            rv_other_movie.hasFixedSize()
            rv_other_movie.adapter = detailAdapter


        }
    }

    private fun stopShimmering() {
        iv_detail_backdrop.stopLoading()
        iv_detail_poster.stopLoading()
        tv_detail_title.stopLoading()
        tv_detail_release_year.stopLoading()
        tv_overview.stopLoading()
        tv_detail_overview.stopLoading()
        tv_interest.stopLoading()
    }
}