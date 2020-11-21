package com.dhimas.dhiflix.ui.search.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.ui.search.SearchViewModel
import com.dhimas.dhiflix.vo.Status
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_search_movie.*

class SearchMovieFragment : Fragment() {
    private val vm: SearchViewModel by viewModels({ requireParentFragment() })

//    //TEKNIK 2
//    companion object {
//        private lateinit var viewModel: SearchMovieViewModel
//
//        fun setSearch(search: String){
//            viewModel.setSearch(search)
//        }
//    }

    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter()

//        //TEKNIK 2
//        val factory = ViewModelFactory.getInstance(requireContext())
//        viewModel = ViewModelProvider(this, factory)[SearchMovieViewModel::class.java]
//        viewModel.getMovies().observe(viewLifecycleOwner, { movieList ->

        vm.getMovies().observe(viewLifecycleOwner, { movieList ->
            when (movieList.status) {
                Status.SUCCESS -> {
                    movieAdapter.addMovie(movieList.data as ArrayList<ShowEntity>)
                    movieAdapter.notifyDataSetChanged()

                    if (!movieList.data.isNullOrEmpty()) {
                        setViewVisibility(
                            loading = false,
                            rvMovie = true,
                            ivIllustration = false,
                            tvInfo = false
                        )
                    } else {
                        setViewVisibility(
                            loading = false,
                            rvMovie = false,
                            ivIllustration = true,
                            tvInfo = true
                        )
                        setInfoImageAndMessage(R.drawable.undraw_not_found_60pq, "No movie found.")
                    }

                }

                Status.LOADING -> {
                    setViewVisibility(
                        loading = true,
                        rvMovie = true,
                        ivIllustration = false,
                        tvInfo = false
                    )
                }

                Status.ERROR -> {
                    setViewVisibility(
                        loading = false,
                        rvMovie = false,
                        ivIllustration = true,
                        tvInfo = true
                    )
                    setInfoImageAndMessage(
                        R.drawable.undraw_not_found_60pq,
                        movieList.message.toString()
                    )
                }
            }
        })

        rv_search_movies.layoutManager = GridLayoutManager(requireContext(), 3)
        rv_search_movies.hasFixedSize()
        rv_search_movies.adapter = movieAdapter
    }

    private fun setViewVisibility(
        loading: Boolean,
        rvMovie: Boolean,
        ivIllustration: Boolean,
        tvInfo: Boolean
    ) {
        progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        rv_search_movies.visibility = if (rvMovie) View.VISIBLE else View.INVISIBLE
        iv_movie_illustration.visibility = if (ivIllustration) View.VISIBLE else View.INVISIBLE
        tv_movie_info.visibility = if (tvInfo) View.VISIBLE else View.INVISIBLE
    }

    private fun setInfoImageAndMessage(image: Int, message: String) {
        val targetWidth = 1361
        val targetHeight = 938
        Picasso.get()
            .load(image)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(targetWidth, targetHeight)
            .into(iv_movie_illustration)
        tv_movie_info.text = message
    }

    override fun onResume() {
        super.onResume()

        if (!vm.getSearchQuery().value.isNullOrEmpty())
            vm.triggerMovie()
    }

//    override fun onPause() {
//        super.onPause()
//
//        if(!vm.getSearchQuery().value.isNullOrEmpty())
//            vm.triggerSeries()
//    }

}