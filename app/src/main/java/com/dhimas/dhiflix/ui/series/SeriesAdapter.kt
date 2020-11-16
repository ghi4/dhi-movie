package com.dhimas.dhiflix.ui.series

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.Utils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class SeriesAdapter : PagedListAdapter<ShowEntity, SeriesAdapter.SeriesViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ShowEntity>() {
            override fun areItemsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return SeriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val seriesEntity = getItem(position) as ShowEntity
        holder.bind(seriesEntity)
    }

    class SeriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(series: ShowEntity) {
            with(itemView) {
                tv_title.text = series.title
                tv_release_date.text = Utils.dateParseToMonthAndYear(series.releaseDate)

                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + series.posterPath)
                    .resize(Constant.POSTER_TARGET_WIDTH, Constant.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(iv_poster)

                cv_poster.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, series.id)

                    //Used for checking if the show entity is from series page
                    intent.putExtra(
                        DetailActivity.EXTRA_SHOW_TYPE,
                        series.show_type
                    )

                    context.startActivity(intent)
                }
            }
        }

    }
}