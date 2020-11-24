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
import com.dhimas.dhiflix.databinding.ActivityDetailBinding
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.utils.Utils.dateParseToMonthAndYear
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.dhimas.dhiflix.utils.Utils.showSnackBar
import com.dhimas.dhiflix.utils.Utils.showToast
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var showId: String
    private lateinit var showEntity: ShowEntity

    private var showType: Int = 0
    private lateinit var detailAdapter: DetailAdapter

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

        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getIntExtra(EXTRA_SHOW_TYPE, 0)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailViewModel::class.java)
        viewModel.setDoubleTrigger(showId, showType)

        setupUI()

        //Delay for shimmer animation
        val minShimmerTime = getMinShimmerTime(viewModel.isAlreadyShimmer)
        Handler(Looper.getMainLooper()).postDelayed({
            viewModelObserveDetail()
            viewModelObserveSimilarList()
            viewModelObservePopularList()
        }, minShimmerTime)
    }

    private fun setupUI() {
        startShimmering()
        startShimmerList()

        detailAdapter = DetailAdapter()

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvDetailOtherShows.layoutManager = layoutManager
            rvDetailOtherShows.hasFixedSize()
            rvDetailOtherShows.adapter = detailAdapter
            rvDetailOtherShows.visibility = View.VISIBLE

            btDetailFavorite.setOnClickListener {
                viewModel.setFavorite(showEntity)
            }
        }
    }

    private fun viewModelObserveDetail() {
        viewModel.getShowEntityById().observe(this, { mShowEntity ->
            when (mShowEntity.status) {
                Status.LOADING -> {
                    startShimmering()
                }

                Status.SUCCESS -> {
                    if (mShowEntity.data != null) {

                        showEntity = mShowEntity.data

                        val btFavoriteText =
                            if (showEntity.isFavorite == 0)
                                getString(R.string.add_to_favorite)
                            else
                                getString(R.string.remove_from_favorite)

                        with(binding) {
                            tvDetailTitle.text = showEntity.title
                            tvDetailReleaseDate.text = dateParseToMonthAndYear(showEntity.releaseDate)
                            tvDetailOverview.text = showEntity.overview
                            btDetailFavorite.text = btFavoriteText

                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + showEntity.backdropPath)
                                .placeholder(R.drawable.backdrop_placeholder)
                                .error(R.drawable.image_error)
                                .resize(
                                    Const.BACKDROP_TARGET_WIDTH,
                                    Const.BACKDROP_TARGET_HEIGHT
                                )
                                .into(ivDetailBackdrop)

                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + showEntity.posterPath)
                                .placeholder(R.drawable.poster_placeholder)
                                .error(R.drawable.poster_error)
                                .resize(
                                    Const.POSTER_TARGET_WIDTH,
                                    Const.POSTER_TARGET_HEIGHT
                                )
                                .into(ivDetailPoster)
                        }

                        stopShimmering()
                    }
                }

                Status.ERROR -> {
                    showSnackBar(
                        binding.scrollviewDetail,
                        mShowEntity.message ?: getString(R.string.unknown_error)
                    ) {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                }
            }
        })

        viewModel.setAlreadyShimmer()
    }

    private fun viewModelObserveSimilarList() {
        viewModel.getSimilarList().observe(this, { movieList ->
            when (movieList.status) {
                Status.LOADING -> {
                    startShimmerList()
                }

                Status.SUCCESS -> {
                    if (movieList.data.isNullOrEmpty()) {
                        showToast(this, getString(R.string.no_similar_list_found))
                        viewModel.setListEmptyTrigger()
                    } else {
                        detailAdapter.setMovies(viewModel.isAlreadyShimmer)
                        detailAdapter.setList(movieList.data as ArrayList<ShowEntity>)
                        detailAdapter.notifyDataSetChanged()
                        stopShimmerList()
                    }
                }

                Status.ERROR -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    showSnackBar(
                        binding.scrollviewDetail,
                        movieList.message ?: getString(R.string.unknown_error)
                    ) {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                }
            }
        })
    }

    private fun viewModelObservePopularList() {
        viewModel.getPopularList().observe(this, { movieList ->
            when (movieList.status) {
                Status.LOADING -> {
                    startShimmerList()
                }

                Status.SUCCESS -> {
                    if (movieList.data.isNullOrEmpty()) {
                        binding.tvDetailInterestTitle.visibility = View.GONE
                        showToast(this, getString(R.string.no_popular_list_found))
                    } else {
                        detailAdapter.setMovies(viewModel.isAlreadyShimmer)
                        detailAdapter.setList(movieList.data as ArrayList<ShowEntity>)
                        detailAdapter.notifyDataSetChanged()
                        stopShimmerList()
                    }
                }

                Status.ERROR -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    showSnackBar(
                        binding.scrollviewDetail,
                        movieList.message ?: getString(R.string.unknown_error)
                    ) {
                        viewModel.setDoubleTrigger(showId, showType)
                    }
                }
            }
        })
    }

    private fun stopShimmering() {
        with(binding) {
            if (ivDetailPoster.isLoading())
                ivDetailPoster.stopLoading()
            tvDetailTitle.stopLoading()
            tvDetailReleaseDate.stopLoading()
            tvDetailOverview.stopLoading()
            tvDetailOverview.stopLoading()
            tvDetailInterestTitle.stopLoading()
            btDetailFavorite.stopLoading()
        }
    }

    private fun startShimmering() {
        with(binding) {
            ivDetailPoster.startLoading()
            tvDetailTitle.startLoading()
            tvDetailReleaseDate.startLoading()
            tvDetailOverviewTitle.startLoading()
            tvDetailOverview.startLoading()
            tvDetailInterestTitle.startLoading()
            btDetailFavorite.startLoading()
        }
    }

    private fun startShimmerList() {
        with(binding) {
            shimmerLayoutDetailOtherShows.visibility = View.VISIBLE
            shimmerLayoutDetailOtherShows.startShimmer()
        }
    }

    private fun stopShimmerList() {
        with(binding) {
            shimmerLayoutDetailOtherShows.visibility = View.GONE
            shimmerLayoutDetailOtherShows.stopShimmer()
        }
    }
}