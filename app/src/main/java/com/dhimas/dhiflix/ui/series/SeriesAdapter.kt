package com.dhimas.dhiflix.ui.series

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.MovieEntity
import kotlinx.android.synthetic.main.item_movie.view.*

class SeriesAdapter: RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {
    private val seriesList = ArrayList<MovieEntity>()

    fun setSeries(series: ArrayList<MovieEntity>){
        this.seriesList.clear()
        this.seriesList.addAll(series)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val seriesEntity = seriesList[position]
        holder.bind(seriesEntity)
    }

    override fun getItemCount(): Int = seriesList.size

    class SeriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(series: MovieEntity){
            with(itemView){
                tv_title.text = series.title
                tv_release_year.text = series.releaseYear
            }
        }

    }
}