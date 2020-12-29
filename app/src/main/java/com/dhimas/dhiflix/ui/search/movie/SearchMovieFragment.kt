package com.dhimas.dhiflix.ui.search.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.data.Resource
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.core.ui.ShowsAdapter
import com.dhimas.dhiflix.core.utils.DataMapper
import com.dhimas.dhiflix.databinding.FragmentSearchMovieBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.ui.search.SearchFragment
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.qualifier.named

@FlowPreview
@ExperimentalCoroutinesApi
class SearchMovieFragment : Fragment() {

    private lateinit var binding: FragmentSearchMovieBinding
    private lateinit var movieAdapter: ShowsAdapter

    //Get the same viewModel instance of SearchFragment as the host
//    private val viewModel: SearchViewModel by lazy { requireParentFragment().getViewModel() }

    private val viewModelScope = getKoin().getScope(SearchFragment.getScopeId())
    private val viewModel: SearchViewModel by viewModelScope.viewModel(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()

        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->
            when (movieList) {
                is Resource.Loading -> {
                    setViewVisibility(loading = true, ivInfo = false, tvInfo = false)
                }

                is Resource.Success -> {
                    val data = movieList.data
                    if (data.isNullOrEmpty()) {
                        setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                        setInfoImageAndMessage(
                            R.drawable.undraw_not_found_60pq,
                            getString(R.string.no_movie_found)
                        )
                    } else {
                        val list = DataMapper.mapListDomainToArrayShowsModel(data)
                        movieAdapter.setList(list)
                        setViewVisibility(loading = false, ivInfo = false, tvInfo = false)
                    }
                }

                is Resource.Error -> {
                    setViewVisibility(loading = false, ivInfo = true, tvInfo = true)
                    setInfoImageAndMessage(
                        R.drawable.undraw_signal_searching_bhpc,
                        movieList.message
                    )
                }
            }
        })
    }

    private fun setupUI() {
        movieAdapter = ShowsAdapter()

        //OnClick go to DetailActivity
        movieAdapter.onItemClick = { selectedItem ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, selectedItem.id)
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, selectedItem.showType)
            startActivity(intent)
        }

        with(binding) {
            rvSearchMovie.layoutManager = GridLayoutManager(requireContext(), 3)
            rvSearchMovie.adapter = movieAdapter
        }
    }

    private fun setViewVisibility(loading: Boolean, ivInfo: Boolean, tvInfo: Boolean) {
        with(binding) {
            pbSearchMovie.visibility = if (loading) View.VISIBLE else View.GONE
            ivSearchMovieInfo.visibility = if (ivInfo) View.VISIBLE else View.INVISIBLE
            tvSearchMovieInfo.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun setInfoImageAndMessage(
        image: Int,
        message: String? = getString(R.string.unknown_error)
    ) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(binding.ivSearchMovieInfo)
        binding.tvSearchMovieInfo.text = message
    }

}