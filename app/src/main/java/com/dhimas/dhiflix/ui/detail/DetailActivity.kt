package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.Utils.dateParseToMonthAndYear
import com.dhimas.dhiflix.utils.Utils.doneDelay
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.utils.Utils.waitDelay
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var viewModel: DetailViewModel
    private lateinit var showId: String
    private lateinit var showEntity1: ShowEntity

    private var showType: Int = 0
    private var detailAdapter: DetailAdapter = DetailAdapter()

    companion object {
        //For sending Show ID
        const val EXTRA_SHOW_ID = "extra_show_id"

        //FOr sending Show Type (Movie/Series)
        const val EXTRA_SHOW_TYPE = "extra_show_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getIntExtra(EXTRA_SHOW_TYPE, 0)
        viewModel.setDoubleTrigger(showId, showType)

        setupUI()

        //Delay for shimmer animation
        waitDelay()
        val minShimmerTime = getMinShimmerTime(viewModel.isAlreadyShimmer)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserveDetail()
            viewModelObserveList()
        }, minShimmerTime)
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_other_movie.layoutManager = layoutManager
        rv_other_movie.hasFixedSize()
        rv_other_movie.adapter = detailAdapter

        bt_favorite.setOnClickListener {
            viewModel.setFavorite(showEntity1)
        }
    }

    private fun viewModelObserveList() {
        viewModel.getShowList().observe(this, { movieList ->
            when (movieList.status) {
                Status.LOADING -> {
                    //startShimmering()
                }

                Status.SUCCESS -> {
                    detailAdapter.setMovies(showType, viewModel.isAlreadyShimmer)
                    detailAdapter.submitList(movieList.data)
                    detailAdapter.notifyDataSetChanged()

                    if (movieList.data.isNullOrEmpty()) {
                        tv_interest.visibility = View.GONE
                        showToast(this, "List failed to load.")
                    }

                    stopShimmering()
                    doneDelay()
                }

                Status.ERROR -> {
                    showToast(this, "List failed to load.")
                    showSnackBar(scrollView, movieList.message ?: "Unknown Error") {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                    doneDelay()
                }
            }
        })
    }

    private fun viewModelObserveDetail() {
        viewModel.getShowEntityById().observe(this, { showEntity ->
            when (showEntity.status) {
                Status.LOADING -> {
                    startShimmering()
                }

                Status.SUCCESS -> {
                    if (showEntity.data != null) {

                        this.showEntity1 = showEntity.data

                        val btFavoriteText =
                            if (showEntity1.isFavorite == 0)
                                getString(R.string.add_to_favorite)
                            else
                                getString(R.string.remove_from_favorite)

                        tv_detail_title.text = showEntity1.title
                        tv_detail_release_date.text =
                            dateParseToMonthAndYear(showEntity1.releaseDate)
                        tv_detail_overview.text = showEntity1.overview
                        bt_favorite.text = btFavoriteText

                        Picasso.get()
                            .load(Constant.URL_BASE_IMAGE + showEntity1.backdropPath)
                            .placeholder(R.drawable.backdrop_placeholder)
                            .error(R.drawable.image_error)
                            .resize(
                                Constant.BACKDROP_TARGET_WIDTH,
                                Constant.BACKDROP_TARGET_HEIGHT
                            )
                            .into(iv_detail_backdrop)

                        Picasso.get()
                            .load(Constant.URL_BASE_IMAGE + showEntity1.posterPath)
                            .placeholder(R.drawable.poster_placeholder)
                            .error(R.drawable.poster_error)
                            .resize(
                                Constant.POSTER_TARGET_WIDTH,
                                Constant.POSTER_TARGET_HEIGHT
                            )
                            .into(iv_detail_poster)

                        stopShimmering()
                    } else {
                        showSnackBar(scrollView, showEntity.message.toString()) {
                            viewModel.setDoubleTrigger(showId, showType)
                        }
                    }
                }

                Status.ERROR -> {
                    showSnackBar(scrollView, showEntity.message.toString()) {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                }
            }
        })

        viewModel.setAlreadyShimmer()
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