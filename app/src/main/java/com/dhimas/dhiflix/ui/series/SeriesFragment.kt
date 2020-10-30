package com.dhimas.dhiflix.ui.series

import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.EspressoIdlingResource
import com.dhimas.dhiflix.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_series.*

class SeriesFragment : Fragment() {
    private lateinit var viewModel: SeriesViewModel
    private lateinit var seriesAdapter: SeriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_series, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (activity != null && view != null) {
            val factory = ViewModelFactory.getInstance()
            viewModel = ViewModelProvider(this, factory)[SeriesViewModel::class.java]
            seriesAdapter = SeriesAdapter()

            EspressoIdlingResource.increment()

            //Prevent re-shimmer when rotating phone
            if (!viewModel.isAlreadyShimmer) {
                //If data loaded too fast causing awkward animation/view
                Handler(Looper.getMainLooper()).postDelayed({
                    viewModelObserve()
                    viewModel.setAlreadyShimmer()
                }, Constant.MINIMUM_SHIMMER_TIME)
            } else {
                seriesShimmerLayout.stopShimmer()
                seriesShimmerLayout.visibility = View.GONE
                viewModelObserve()
            }

            //Change grid layout spanCount when Landscape/Portrait
            val phoneOrientation = requireActivity().resources.configuration.orientation
            if (phoneOrientation == Configuration.ORIENTATION_PORTRAIT) {
                rv_series.layoutManager = GridLayoutManager(context, 3)
            } else {
                rv_series.layoutManager = GridLayoutManager(context, 7)
            }

            rv_series.hasFixedSize()
            rv_series.adapter = seriesAdapter
        }
    }

    private fun viewModelObserve() {
        if (view != null) {
            viewModel.getSeries().observe(viewLifecycleOwner, { seriesList ->
                seriesAdapter.setSeries(seriesList as ArrayList<ShowEntity>)
                seriesShimmerLayout.stopShimmer()
                seriesShimmerLayout.visibility = View.GONE
                seriesAdapter.notifyDataSetChanged()

                EspressoIdlingResource.decrement()
            })
        }
    }
}