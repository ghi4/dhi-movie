package com.dhimas.dhiflix.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dhimas.dhiflix.ui.search.movie.SearchMovieFragment
import com.dhimas.dhiflix.ui.search.series.SearchSeriesFragment

class ViewPagerAdapter(fa: FragmentActivity, private val viewModel: SearchViewModel) :
    FragmentStateAdapter(fa) {

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