package com.dhimas.dhiflix.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dhimas.dhiflix.ui.search.movie.SearchMovieFragment
import com.dhimas.dhiflix.ui.search.series.SearchSeriesFragment

class ViewPagerAdapter(fm: FragmentManager, lifecycle: Lifecycle, private val viewModel: SearchViewModel) :
    FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when (position) {
            0 -> fragment = SearchMovieFragment.newInstance(viewModel)
            1 -> fragment = SearchSeriesFragment.newInstance(viewModel)
        }

        return fragment as Fragment
    }
}