package com.dhimas.dhiflix.ui.series

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ItemShowBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Const
import com.dhimas.dhiflix.utils.Utils.dateParseToMonthAndYear
import com.squareup.picasso.Picasso

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>(){
    private var seriesList = ArrayList<ShowEntity>()

    fun addSeries(series: ArrayList<ShowEntity>){
        seriesList.clear()
        seriesList.addAll(series)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        holder.bind(seriesList[position])
    }

    override fun getItemCount(): Int = seriesList.size

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowBinding.bind(itemView)
        fun bind(series: ShowEntity) {
            with(binding) {
                tvTitle.text = series.title
                tvReleaseDate.text = dateParseToMonthAndYear(series.releaseDate)

                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + series.posterPath)
                    .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPoster)

                cvPoster.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, series.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, series.showType)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }
}