package com.dhimas.dhiflix.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slider_fragment.*

class SliderFragment : Fragment() {

    companion object {
        private lateinit var showEntity: ShowEntity

        fun newInstance(showEntity: ShowEntity): SliderFragment{
            val fragment = SliderFragment()

            Companion.showEntity = showEntity

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.slider_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get()
            .load(Constant.URL_BASE_IMAGE + showEntity.backdropPath)
            .placeholder(R.drawable.backdrop_placeholder)
            .error(R.drawable.image_error)
            .resize(Constant.BACKDROP_TARGET_WIDTH, Constant.BACKDROP_TARGET_HEIGHT)
            .into(iv_slider)

        tv_slider_title.text = showEntity.title

        slider_container.setOnClickListener {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)

            //Used for checking if the show entity is from movie page
            intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, showEntity.show_type)

            startActivity(intent)
        }
    }

}