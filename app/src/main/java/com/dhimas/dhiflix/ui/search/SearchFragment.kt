package com.dhimas.dhiflix.ui.search

import android.os.Bundle
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
import org.koin.android.ext.android.getKoin
import org.koin.android.viewmodel.scope.viewModel
import org.koin.core.qualifier.named

@ExperimentalCoroutinesApi
@FlowPreview
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModelScope = getKoin().getOrCreateScope(getScopeId(), named(getScopeName()))
    private val viewModel: SearchViewModel by viewModelScope.viewModel(this)

    companion object {
        private const val SCOPE_ID = "Search"
        private const val SCOPE_NAME = "SearchViewModel"

        fun getScopeId() = SCOPE_ID

        fun getScopeName() = SCOPE_NAME
    }

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
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.queryChannel.send(newText.toString())
                }
                return false
            }
        })
    }
}