package com.dhimas.dhiflix.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity

class SliderAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private var sliderEntities = ArrayList<ShowEntity>()

    fun deleteShow() {
        sliderEntities.clear()
    }

    fun addShow(showEntity: ShowEntity){
        sliderEntities.add(showEntity)
    }

    override fun getItemCount(): Int = sliderEntities.size

    override fun createFragment(position: Int): Fragment {
        return SliderFragment.newInstance(sliderEntities[position])
    }

}