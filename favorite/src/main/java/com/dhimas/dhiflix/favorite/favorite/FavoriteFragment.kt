package com.dhimas.dhiflix.favorite.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.ui.ShowsPosterAdapter
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.favorite.databinding.FragmentFavoriteBinding
import com.dhimas.dhiflix.favorite.di.favoriteModule
import com.dhimas.dhiflix.ui.detail.DetailActivity
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.qualifier.named

class FavoriteFragment : Fragment() {

    //Scope and Koin DI for ViewModel
    private val scopeId = "FavoriteScope"
    private val moduleFavorite = getKoin().getOrCreateScope(scopeId, named(Const.VIEW_MODEL))
    private val viewModel: FavoriteViewModel by moduleFavorite.viewModel(this)

    //Binding
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    //Adapter
    private lateinit var favoriteMovieAdapter: ShowsPosterAdapter
    private lateinit var favoriteSeriesAdapter: ShowsPosterAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadKoinModules(favoriteModule)

        setupUI()
        viewModelObserveMovies() //Load favorite movie list
        viewModelObserveSeries() //Load favorite series list
    }

    private fun setupUI() {
        //Movie
        favoriteMovieAdapter = ShowsPosterAdapter()
        favoriteMovieAdapter.onItemClick = { selectedShow ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }

        //Series
        favoriteSeriesAdapter = ShowsPosterAdapter()
        favoriteSeriesAdapter.onItemClick = { selectedShow ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, selectedShow.id)
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, selectedShow.showType)
            startActivity(intent)
        }


        with(binding) {
            //Movie
            rvFavoriteMovie.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvFavoriteMovie.hasFixedSize()
            rvFavoriteMovie.adapter = favoriteMovieAdapter

            //Series
            rvFavoriteSeries.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            rvFavoriteSeries.hasFixedSize()
            rvFavoriteSeries.adapter = favoriteSeriesAdapter
        }
    }

    private fun viewModelObserveMovies() {
        viewModel.getFavoriteMovies().observe(viewLifecycleOwner, { favoriteMovieList ->
            when (favoriteMovieList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = true, tvInfo = true)
                }
                is Resource.Success -> {
                    val data = favoriteMovieList.data

                    //Loading gone whenever empty or not
                    binding.pbFavorite.visibility = View.GONE

                    //When data is not null or empty
                    //Visible : Title and recyclerView
                    //Gone    : Loading, illustration, message
                    if (!data.isNullOrEmpty()) {
                        favoriteMovieAdapter.setList(data as ArrayList<Show>)
                        favoriteMovieAdapter.setShimmer(false)

                        setMovieViewVisibility(tvMovie = true, rvMovie = true)
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }
                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    binding.tvFavoriteInfo.text = favoriteMovieList.message
                }
            }
        })
    }

    private fun viewModelObserveSeries() {
        viewModel.getFavoriteSeries().observe(viewLifecycleOwner, { favoriteSeriesList ->
            when (favoriteSeriesList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = true, tvInfo = true)
                }
                is Resource.Success -> {
                    val data = favoriteSeriesList.data


                    //Loading gone whenever empty or not
                    binding.pbFavorite.visibility = View.GONE

                    //When data is not null or empty
                    //Visible : Title and recyclerView
                    //Gone    : Loading, illustration, message
                    if (!data.isNullOrEmpty()) {
                        favoriteSeriesAdapter.setList(data as ArrayList<Show>)
                        favoriteSeriesAdapter.setShimmer(false)

                        setSeriesViewVisibility(tvSeries = true, rvSeries = true)
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }
                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    binding.tvFavoriteInfo.text = favoriteSeriesList.message
                }
            }
        })
    }

    //Information: Loading, message, illustration
    private fun setViewVisibility(loading: Boolean, ivInfo: Boolean, tvInfo: Boolean) {
        with(binding) {
            pbFavorite.visibility = if (loading) View.VISIBLE else View.GONE
            ivFavoriteInfo.visibility = if (ivInfo) View.VISIBLE else View.GONE
            tvFavoriteInfo.visibility = if (tvInfo) View.VISIBLE else View.GONE
        }
    }

    //Movie: Title and recyclerView
    private fun setMovieViewVisibility(tvMovie: Boolean, rvMovie: Boolean) {
        with(binding) {
            tvFavoriteMovieTitle.visibility = if (tvMovie) View.VISIBLE else View.GONE
            rvFavoriteMovie.visibility = if (rvMovie) View.VISIBLE else View.GONE
        }
    }

    //Series: Title and recyclerView
    private fun setSeriesViewVisibility(tvSeries: Boolean, rvSeries: Boolean) {
        with(binding) {
            tvFavoriteSeriesTitle.visibility = if (tvSeries) View.VISIBLE else View.GONE
            rvFavoriteSeries.visibility = if (rvSeries) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        setMovieViewVisibility(tvMovie = false, rvMovie = false)
        setSeriesViewVisibility(tvSeries = false, rvSeries = false)
        setViewVisibility(loading = true, ivInfo = false, tvInfo = false)
        viewModel.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.root.removeAllViewsInLayout()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        moduleFavorite.close()
    }

}