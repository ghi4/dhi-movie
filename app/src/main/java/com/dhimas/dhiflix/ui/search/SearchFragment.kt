package com.dhimas.dhiflix.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.ui.movie.MovieAdapter
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager2.adapter = ViewPagerAdapter(requireActivity(), null)

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Movies"
                1 -> tab.text = "Series"
            }
        }.attach()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        movieAdapter = MovieAdapter()

        searchingX.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                setupViewPager(text.toString())
            }
        }
    }

    private fun setupViewPager(keyword: String) {
        viewPager2.adapter = ViewPagerAdapter(requireActivity(), keyword)
    }

}