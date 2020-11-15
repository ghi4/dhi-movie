package com.dhimas.dhiflix.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dhimas.dhiflix.R

class SliderFragment : Fragment() {

    companion object {
        fun newInstance() = SliderFragment()
    }

    private lateinit var viewModel: SliderViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.slider_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SliderViewModel::class.java)
        // TODO: Use the ViewModel
    }

}