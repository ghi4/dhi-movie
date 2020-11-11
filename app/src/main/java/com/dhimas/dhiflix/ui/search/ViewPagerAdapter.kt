package com.dhimas.dhiflix.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dhimas.dhiflix.ui.search.movie.SearchMovieFragment
import com.dhimas.dhiflix.ui.search.series.SearchSeriesFragment

class ViewPagerAdapter(fa: FragmentActivity, private val keyword: String?): FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null

        when(position){
            0 -> fragment = SearchMovieFragment.newInstance(keyword)
            1 -> fragment = SearchSeriesFragment.newInstance(keyword)
        }

        return fragment as Fragment
    }
}