package com.dhimas.dhiflix.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.databinding.FragmentSearchBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
@FlowPreview
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by sharedViewModel()

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
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                lifecycleScope.launch {
                    viewModel.queryChannel.send(newText.toString())
                }
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()

        Log.d("KEPOO", "SEARCH - Resume")
    }

    override fun onPause() {
        super.onPause()

        Log.d("KEPOO", "SEARCH - Pause")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("KEPOO", "SEARCH - Destroy")
    }
}