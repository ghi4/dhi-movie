package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        //For sending Show Title
        const val EXTRA_SHOW_TITLE = "extra_show_title"

        //For checking if Show Title is from Movie Fragment
        const val EXTRA_FROM_MOVIES = "extra_from_movies"

        //For checking if Show Title is from Series Fragment
        const val EXTRA_FROM_SERIES = "extra_from_series"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        val showTitle = intent.getStringExtra(EXTRA_SHOW_TITLE)

        if (showTitle != null) {
            val showEntity = viewModel.getShowEntityByTitle(showTitle)

            tv_detail_title.text = showEntity.title
            tv_detail_release_year.text = showEntity.releaseYear
            tv_detail_overview.text = showEntity.overview

            //For backdrop image
            val backdropTargetWidth = 800
            val backdropTargetHeight = 450

            Picasso.get()
                .load(showEntity.backdropPath!!)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .resize(backdropTargetWidth, backdropTargetHeight)
                .into(iv_detail_backdrop)

            //For poster image
            val posterTargetWidth = 200
            val posterTargetHeight = 300

            Picasso.get()
                .load(showEntity.posterPath!!)
                .placeholder(R.drawable.placeholder_2_3)
                .error(R.drawable.image_error_2_3)
                .resize(posterTargetWidth, posterTargetHeight)
                .into(iv_detail_poster)

            val detailAdapter = DetailAdapter()

            if (intent.extras!!.containsKey(EXTRA_FROM_MOVIES)) {
                val movies = viewModel.getMovieButExclude(showEntity)
                detailAdapter.setMovies(movies, EXTRA_FROM_MOVIES)
            } else if (intent.extras!!.containsKey(EXTRA_FROM_SERIES)) {
                val movies = viewModel.getSeriesButExclude(showEntity)
                detailAdapter.setMovies(movies, EXTRA_FROM_SERIES)
            }

            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_other_movie.layoutManager = layoutManager
            rv_other_movie.hasFixedSize()
            rv_other_movie.adapter = detailAdapter
        }
    }
}