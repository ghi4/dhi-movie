package com.dhimas.dhiflix.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        viewPager2.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = "Movies"
                1 -> tab.text = "Series"
            }
        }.attach()
        viewPager2.offscreenPageLimit = 2

        searchingX.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.toString().isNotEmpty()) {
                    viewModel.setSearchQuery(query.toString())

                    if (viewPager2.currentItem == 0)
                        viewModel.triggerMovie()
                    else
                        viewModel.triggerSeries()

//                    //TEKNIK 2
//                    if(viewPager2.currentItem == 0)
//                        SearchMovieFragment.setSearch(query.toString())
//                    else
//                        SearchSeriesFragment.setSearch(query.toString())


                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}