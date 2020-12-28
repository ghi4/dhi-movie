package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.ui.ShowsPosterAdapter
import com.dhimas.dhiflix.databinding.ActivityDetailBinding
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.utils.Utils.dateParseToMonthAndYear
import com.dhimas.dhiflix.utils.Utils.showToast
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var showId: String
    private lateinit var show: Show
    private val viewModel: DetailViewModel by viewModel()

    private var showType: Int = 0
    private lateinit var showsPosterAdapter: ShowsPosterAdapter

    companion object {
        //For sending Show ID
        const val EXTRA_SHOW_ID = "extra_show_id"

        //FOr sending Show Type (Movie/Series)
        const val EXTRA_SHOW_TYPE = "extra_show_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        //Binding
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get intent showId and showType
        showId = intent.getStringExtra(EXTRA_SHOW_ID).toString()
        showType = intent.getIntExtra(EXTRA_SHOW_TYPE, 0)

        //Set showId and showType
        viewModel.setShowIdAndType(showId, showType)

        setupUI()

        viewModelObserveDetail()
        viewModelObserveSimilarList()
        viewModelObservePopularList() //Backup when similar list is empty
    }

    private fun setupUI() {
        startShimmering() //Shimmer for detail show
        startShimmerList() //Shimmer for similar list

        showsPosterAdapter = ShowsPosterAdapter()
        showsPosterAdapter.onItemClick = { selectedShow ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvDetailOtherShows.layoutManager = layoutManager
            rvDetailOtherShows.hasFixedSize()
            rvDetailOtherShows.adapter = showsPosterAdapter
            rvDetailOtherShows.visibility = View.VISIBLE

            btDetailFavorite.setOnClickListener {
                viewModel.setFavorite(show)
            }
        }
    }

    private fun viewModelObserveDetail() {
        viewModel.getShow().observe(this, { mShow ->
            when (mShow) {
                is Resource.Loading -> {
                    startShimmering()
                }

                is Resource.Success -> {
                    if (mShow.data != null) {
                        show = mShow.data as Show

                        val btFavoriteText =
                            if (show.isFavorite == 0)
                                getString(R.string.add_to_favorite)
                            else
                                getString(R.string.remove_from_favorite)

                        with(binding) {
                            tvDetailTitle.text = show.title
                            tvDetailReleaseDate.text =
                                dateParseToMonthAndYear(show.releaseDate)
                            tvDetailOverview.text = show.overview
                            btDetailFavorite.text = btFavoriteText

                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + show.backdropPath)
                                .placeholder(R.drawable.backdrop_placeholder)
                                .error(R.drawable.image_error)
                                .resize(
                                    Const.BACKDROP_TARGET_WIDTH,
                                    Const.BACKDROP_TARGET_HEIGHT
                                )
                                .into(ivDetailBackdrop)

                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + show.posterPath)
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

                is Resource.Error -> {
                    Snackbar.make(
                        binding.root,
                        mShow.message ?: getString(R.string.unknown_error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("RETRY") {
                            viewModel.setShowIdAndType(showId, showType)
                        }.show()
                }
            }
        })

        viewModel.setAlreadyShimmer()
    }

    private fun viewModelObserveSimilarList() {
        viewModel.getSimilarList().observe(this, { movieList ->
            when (movieList) {
                is Resource.Loading -> {
                    startShimmerList()
                }

                is Resource.Success -> {
                    if (movieList.data.isNullOrEmpty()) {
                        showToast(this, getString(R.string.no_similar_list_found))
                        viewModel.setListEmptyTrigger()
                    } else {
                        showsPosterAdapter.setShimmer(viewModel.isAlreadyShimmer)
                        showsPosterAdapter.setList(movieList.data as ArrayList<Show>)
                        showsPosterAdapter.notifyDataSetChanged()
                        stopShimmerList()
                    }
                }

                is Resource.Error -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    Snackbar.make(
                        binding.root,
                        movieList.message ?: getString(R.string.unknown_error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("RETRY") {
                            viewModel.setShowIdAndType(showId, showType)
                        }.show()
                }
            }
        })
    }

    private fun viewModelObservePopularList() {
        viewModel.getPopularList().observe(this, { movieList ->
            when (movieList) {
                is Resource.Loading -> {
                    startShimmerList()
                }

                is Resource.Success -> {
                    if (movieList.data.isNullOrEmpty()) {
                        binding.tvDetailInterestTitle.visibility = View.GONE
                        showToast(this, getString(R.string.no_popular_list_found))
                    } else {
                        showsPosterAdapter.setShimmer(viewModel.isAlreadyShimmer)
                        showsPosterAdapter.setList(movieList.data as ArrayList<Show>)
                        showsPosterAdapter.notifyDataSetChanged()
                        stopShimmerList()
                    }
                }

                is Resource.Error -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    Snackbar.make(
                        binding.root,
                        movieList.message ?: getString(R.string.unknown_error),
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction("RETRY") {
                            viewModel.setShowIdAndType(showId, showType)
                        }.show()
                }
            }
        })
    }

    private fun stopShimmering() {
        with(binding) {
            ivDetailPoster.stopLoading()
            tvDetailTitle.stopLoading()
            tvDetailReleaseDate.stopLoading()
            tvDetailOverviewTitle.stopLoading()
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

    override fun onPause() {
        super.onPause()
        finish()
    }

}