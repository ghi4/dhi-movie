package com.dhimas.dhiflix.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object{
        const val EXTRA_MOVIE_ENTITY = "extra_movie_entity"
        const val EXTRA_MESSAGE = "extra_message"
        const val EXTRA_FROM_MOVIES = "extra_from_movies"
        const val EXTRA_FROM_SERIES = "extra_from_series"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movieEntity = intent.getParcelableExtra<MovieEntity>(EXTRA_MOVIE_ENTITY)

        if(movieEntity != null){
            tv_detail_title.text = movieEntity.title
            tv_detail_release_year.text = movieEntity.releaseYear
            tv_detail_overview.text = movieEntity.overview

            //For backdrop image
            val backdropTargetWidth = 800
            val backdropTargetHeight = 450

            Picasso.get()
                .load(movieEntity.backdropPath!!)
                .placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_error)
                .resize(backdropTargetWidth, backdropTargetHeight)
                .into(iv_detail_backdrop)

            //For poster image
            val posterTargetWidth = 200
            val posterTargetHeight = 300

            Picasso.get()
                .load(movieEntity.posterPath!!)
                .placeholder(R.drawable.placeholder_2_3)
                .error(R.drawable.image_error_2_3)
                .resize(posterTargetWidth, posterTargetHeight)
                .into(iv_detail_poster)

            val viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

            val detailAdapter = DetailAdapter()

            if(intent.extras!!.containsKey(EXTRA_FROM_MOVIES)){
                val movies = viewModel.getMovieButExclude(movieEntity)
                detailAdapter.setMovies(movies)
            }else if(intent.extras!!.containsKey(EXTRA_FROM_SERIES)){
                val movies = viewModel.getSeriesButExclude(movieEntity)
                detailAdapter.setMovies(movies)
            }

            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rv_other_movie.layoutManager = layoutManager
            rv_other_movie.hasFixedSize()
            rv_other_movie.adapter = detailAdapter
        }
    }
}