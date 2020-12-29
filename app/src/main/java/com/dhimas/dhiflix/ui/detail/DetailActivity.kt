package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.presenter.ShowsPosterAdapter
import com.dhimas.dhiflix.core.presenter.model.ShowsPosterModel
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.core.utils.DataMapper
import com.dhimas.dhiflix.core.utils.Utils.dateParseToMonthAndYear
import com.dhimas.dhiflix.databinding.ActivityDetailBinding
import com.dhimas.dhiflix.utils.Utils.showToast
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.qualifier.named

class DetailActivity : AppCompatActivity() {

    private val scopeId = "DetailScope"
    private val moduleDetail = getKoin().getOrCreateScope(scopeId, named(Const.VIEW_MODEL))
    private val viewModel: DetailViewModel by moduleDetail.viewModel(this)

    private lateinit var binding: ActivityDetailBinding
    private lateinit var similarShowsAdapter: ShowsPosterAdapter
    private lateinit var showId: String
    private var showType: Int = 0


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

        //Set showId and showType to triggering load data
        viewModel.setShowIdAndType(showId, showType)

        setupUI()

        viewModelObserveDetail() //Load data for detail show
        viewModelObserveSimilarList() //Load data for similar show
        viewModelObservePopularList() //Backup data when similar list is empty or failed
    }

    private fun setupUI() {
        startShimmering() //Shimmer for detail show
        startShimmerList() //Shimmer for similar list

        //Initialize adapter for similar shows
        similarShowsAdapter = ShowsPosterAdapter()
        similarShowsAdapter.onItemClick = { selectedShow ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }

        //Setup recyclerView
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvDetailOtherShows.layoutManager = layoutManager
            rvDetailOtherShows.hasFixedSize()
            rvDetailOtherShows.adapter = similarShowsAdapter
        }
    }


    private fun viewModelObserveDetail() {
        viewModel.getShow().observe(this, { mShow ->
            when (mShow) {
                is Resource.Loading -> {
                    startShimmering()
                }

                is Resource.Success -> {
                    val data = mShow.data
                    if (data != null) {
                        val show = DataMapper.mapDomainToShowsDetail(data)

                        //Favorite button text value
                        val btFavoriteText =
                            if (show.isFavorite == 0)
                                getString(R.string.add_to_favorite)
                            else
                                getString(R.string.remove_from_favorite)

                        with(binding) {
                            tvDetailTitle.text = show.title
                            tvDetailReleaseDate.text = dateParseToMonthAndYear(show.releaseDate)
                            tvDetailOverview.text = show.overview
                            btDetailFavorite.text = btFavoriteText

                            //Backdrop
                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + show.backdropPath)
                                .placeholder(R.drawable.backdrop_placeholder)
                                .error(R.drawable.image_error)
                                .resize(Const.BACKDROP_TARGET_WIDTH, Const.BACKDROP_TARGET_HEIGHT)
                                .into(ivDetailBackdrop)

                            //Poster
                            Picasso.get()
                                .load(Const.URL_BASE_IMAGE + show.posterPath)
                                .placeholder(R.drawable.poster_placeholder)
                                .error(R.drawable.poster_error)
                                .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                                .into(ivDetailPoster)

                            btDetailFavorite.setOnClickListener {
                                viewModel.setFavorite(data)
                            }
                        }
                        stopShimmering()
                    }
                }

                is Resource.Error -> {
                    showSnackBar(mShow.message) //Show snackbar for retry load data
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
                    val data = movieList.data
                    if (data == null) {
                        showToast(this, getString(R.string.no_similar_list_found))
                        viewModel.setListEmptyTrigger() //Trigger popular list
                    } else {
                        similarShowsAdapter.setShimmer(viewModel.getIsAlreadyShimmer())

                        val list = data.map { DataMapper.mapDomainToShowsPoster(it) }
                        similarShowsAdapter.setList(list as ArrayList<ShowsPosterModel>)

                        stopShimmerList()
                    }
                }

                is Resource.Error -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    showSnackBar(movieList.message) //Show snackbar for retry load data
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
                    val data = movieList.data
                    if (data == null) {
                        binding.tvDetailInterestTitle.visibility = View.GONE
                        showToast(this, getString(R.string.no_popular_list_found))
                    } else {
                        similarShowsAdapter.setShimmer(viewModel.getIsAlreadyShimmer())

                        val list = data.map { DataMapper.mapDomainToShowsPoster(it) }
                        similarShowsAdapter.setList(list as ArrayList<ShowsPosterModel>)

                        stopShimmerList()
                    }
                }

                is Resource.Error -> {
                    showToast(this, getString(R.string.list_failed_to_load))
                    showSnackBar(movieList.message) //Show snackbar for retry load data
                }
            }
        })
    }

    private fun showSnackBar(message: String?) {
        val safeMessage = message ?: getString(R.string.unknown_error)
        Snackbar.make(binding.root, safeMessage, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                viewModel.setShowIdAndType(showId, showType)
            }.show()
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

    override fun onDestroy() {
        super.onDestroy()
        moduleDetail.close()
    }

}