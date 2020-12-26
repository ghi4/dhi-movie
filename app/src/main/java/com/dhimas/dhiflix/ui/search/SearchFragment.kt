package com.dhimas.dhiflix.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpSearch.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabsSearch, binding.vpSearch) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.movies)
                1 -> tab.text = getString(R.string.series)
            }
        }.attach()
        binding.vpSearch.offscreenPageLimit = 2

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.toString().isNotEmpty()) {
                    Log.d("KKWP", "Search - $query")
                    viewModel.setSearchQuery(query.toString())
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}