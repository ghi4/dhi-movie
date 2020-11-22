package com.dhimas.dhiflix.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ActivityDetailBinding
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

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
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

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)

        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getIntExtra(EXTRA_SHOW_TYPE, 0)
        viewModel.setDoubleTrigger(showId, showType)

        binding.similarShimmer.startShimmer()

        setupUI()

        //Delay for shimmer animation
        waitDelay()
        val minShimmerTime = getMinShimmerTime(viewModel.isAlreadyShimmer)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserveDetail()
            //viewModelObserveSimilarList()
            viewModelObservePopularList()
            viewModel.listEmptyTrigger()
        }, minShimmerTime)
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvOtherMovie.layoutManager = layoutManager
            rvOtherMovie.hasFixedSize()
            rvOtherMovie.adapter = detailAdapter
            rvOtherMovie.visibility = View.VISIBLE

            btFavorite.setOnClickListener {
                viewModel.setFavorite(showEntity1)
            }
        }
    }

    private fun viewModelObserveSimilarList() {
        viewModel.getSimilarList().observe(this, { movieList ->
            Log.d("WKS", "IN SIMILAR")
            when (movieList.status) {
                Status.LOADING -> {
                    startShimmerList()
                }

                Status.SUCCESS -> {
                    detailAdapter.setMovies(viewModel.isAlreadyShimmer)
                    detailAdapter.setList(movieList.data as ArrayList<ShowEntity>)
                    detailAdapter.notifyDataSetChanged()
                    stopShimmerList()
                    doneDelay()
                    if (movieList.data.isNullOrEmpty()) {
                        //tv_interest.visibility = View.GONE
                        showToast(this, "No similar list found.")
                        viewModel.listEmptyTrigger()
                    }
                }

                Status.ERROR -> {
                    showToast(this, "List failed to load.")
                    showSnackBar(binding.scrollView, movieList.message ?: "Unknown Error") {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                    doneDelay()
                }
            }
        })
    }

    private fun viewModelObservePopularList() {
        viewModel.getPopularList().observe(this, { movieList ->
            Log.d("WKS", "IN POPULAR")
            when (movieList.status) {
                Status.LOADING -> {
                    startShimmerList()
                }

                Status.SUCCESS -> {
                    Log.d("KKK", "SIZE: " + movieList.data?.size)
                    Log.d("KKK", "MSG: " + movieList.message)

                    detailAdapter.setMovies(viewModel.isAlreadyShimmer)
                    detailAdapter.setList(movieList.data as ArrayList<ShowEntity>)
                    detailAdapter.notifyDataSetChanged()
//                    stopShimmerList()
                    doneDelay()

                    Log.d("KKK", "SIZE: " + movieList.data.size)
                    Log.d("KKK", "MSG: " + movieList.message)

                    if (movieList.data.isNullOrEmpty()) {
                        //tv_interest.visibility = View.GONE
                        showToast(this, "No popular list found.")
                    } else {
                        detailAdapter.setMovies(viewModel.isAlreadyShimmer)
                        detailAdapter.setList(movieList.data)
                        detailAdapter.notifyDataSetChanged()
                        stopShimmerList()
                        doneDelay()
                    }
                }

                Status.ERROR -> {
                    showToast(this, "List failed to load.")
                    showSnackBar(binding.scrollView, movieList.message ?: "Unknown Error") {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                    doneDelay()
                }
            }
        })
    }

    private fun viewModelObserveDetail() {
        viewModel.getShowEntityById().observe(this, { showEntity ->
            Log.d("WKS", "DETAIL")
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

                        with(binding) {
                            tvDetailTitle.text = showEntity1.title
                            tvDetailReleaseDate.text =
                                dateParseToMonthAndYear(showEntity1.releaseDate)
                            tvDetailOverview.text = showEntity1.overview
                            btFavorite.text = btFavoriteText

                            Picasso.get()
                                .load(Constant.URL_BASE_IMAGE + showEntity1.backdropPath)
                                .placeholder(R.drawable.backdrop_placeholder)
                                .error(R.drawable.image_error)
                                .resize(
                                    Constant.BACKDROP_TARGET_WIDTH,
                                    Constant.BACKDROP_TARGET_HEIGHT
                                )
                                .into(ivDetailBackdrop)

                            Picasso.get()
                                .load(Constant.URL_BASE_IMAGE + showEntity1.posterPath)
                                .placeholder(R.drawable.poster_placeholder)
                                .error(R.drawable.poster_error)
                                .resize(
                                    Constant.POSTER_TARGET_WIDTH,
                                    Constant.POSTER_TARGET_HEIGHT
                                )
                                .into(ivDetailPoster)
                        }

                        stopShimmering()
                    } else {
                        showSnackBar(binding.scrollView, showEntity.message.toString()) {
                            viewModel.setDoubleTrigger(showId, showType)
                        }
                    }
                }

                Status.ERROR -> {
                    showSnackBar(binding.scrollView, showEntity.message.toString()) {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                }
            }
        })

        viewModel.setAlreadyShimmer()
    }

    private fun stopShimmering() {
        with(binding) {
            ivDetailPoster.stopLoading()
            tvDetailTitle.stopLoading()
            tvDetailReleaseDate.stopLoading()
            tvOverview.stopLoading()
            tvDetailOverview.stopLoading()
            tvInterest.stopLoading()
            btFavorite.stopLoading()
        }
    }

    private fun startShimmering() {
        with(binding) {
            ivDetailPoster.startLoading()
            tvDetailTitle.startLoading()
            tvDetailReleaseDate.startLoading()
            tvOverview.startLoading()
            tvDetailOverview.startLoading()
            tvInterest.startLoading()
            btFavorite.startLoading()
        }
    }

    private fun startShimmerList() {
        with(binding) {
            similarShimmer.visibility = View.VISIBLE
            similarShimmer.startShimmer()
        }
    }

    private fun stopShimmerList() {
        with(binding) {
            similarShimmer.visibility = View.GONE
            similarShimmer.stopShimmer()
        }
    }
}