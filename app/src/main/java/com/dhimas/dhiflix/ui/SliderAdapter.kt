package com.dhimas.dhiflix.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.slider_fragment.view.*

class SliderAdapter(val context: Context): RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    var sliderEntities = ArrayList<ShowEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.slider_fragment, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderEntities[position])
    }

    override fun getItemCount(): Int = sliderEntities.size

    class SliderViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(showEntity: ShowEntity){
            with(itemView){
                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + showEntity.backdropPath)
                    .into(iv_slider)

                tv_slider_title.text = showEntity.title

                setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)

                    //Used for checking if the show entity is from movie page
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, showEntity.show_type)

                    startActivity(context, intent, null)
                }
            }
        }
    }

}