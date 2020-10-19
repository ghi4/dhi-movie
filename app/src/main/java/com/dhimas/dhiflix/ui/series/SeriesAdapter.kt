package com.dhimas.dhiflix.ui.series

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.MovieEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.squareup.picasso.Picasso
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

                Picasso.get()
                        .load(series.posterPath!!)
                        .resize(200,300)
                        .error(R.drawable.image_error_2_3)
                        .placeholder(R.drawable.placeholder_2_3)
                        .into(iv_poster)

                cv_poster.setOnClickListener{
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_MOVIE_ENTITY, series)
                    intent.putExtra(DetailActivity.EXTRA_FROM_SERIES, DetailActivity.EXTRA_MESSAGE)
                    context.startActivity(intent)
                }
            }
        }

    }
}