package com.dhimas.dhiflix.ui.series

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class SeriesAdapter : RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {
    private val seriesList = ArrayList<ShowEntity>()

    fun setSeries(series: ArrayList<ShowEntity>) {
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

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(series: ShowEntity) {
            with(itemView) {
                tv_title.text = series.title
                tv_release_year.text = series.releaseYear

                val posterTargetWidth = 200
                val posterTargetHeight = 300

                Picasso.get()
                        .load("https://image.tmdb.org/t/p/w500" + series.posterPath!!)
                        .resize(posterTargetWidth, posterTargetHeight)
                        .error(R.drawable.image_error_2_3)
                        .placeholder(R.drawable.poster_placeholder)
                        .into(iv_poster)

                cv_poster.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, series.id)

                    //Used for checking if the show entity is from series page
                    //Sending empty value because I use key for checking without read the data
                    intent.putExtra(
                            DetailActivity.EXTRA_SHOW_TYPE,
                            DetailActivity.EXTRA_FROM_SERIES
                    )

                    context.startActivity(intent)
                }
            }
        }

    }
}