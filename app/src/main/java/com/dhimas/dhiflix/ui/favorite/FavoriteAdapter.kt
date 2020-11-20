package com.dhimas.dhiflix.ui.favorite

import android.content.Intent
import android.os.Handler
import android.os.Looper
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
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class FavoriteAdapter internal constructor() :
    PagedListAdapter<ShowEntity, FavoriteAdapter.ShowViewHolder>(DIFF_CALLBACK) {
    private var listType = Constant.MOVIE_TYPE //Default type is movie
    private var isAlreadyShimmer: Boolean = false

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

    fun setMovies(listType: Int, isAlreadyShimmer: Boolean) {
        this.listType = listType
        this.isAlreadyShimmer = isAlreadyShimmer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_horizontal, parent, false)

        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val showEntity = getItem(position)
        if (showEntity != null) {
            holder.bind(showEntity, listType, isAlreadyShimmer)
        }
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(showEntity: ShowEntity, type: Int, isAlreadyShimmer: Boolean) {
            with(itemView) {

                //Start shimmer
                iv_poster_horizontal.startLoading()

                //Horizontal Poster
                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + showEntity.posterPath!!)
                    .resize(Constant.POSTER_TARGET_WIDTH, Constant.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(iv_poster_horizontal)

                //Delay for shimmer animation
                val minShimmerTime = getMinShimmerTime(isAlreadyShimmer)
                Handler(Looper.getMainLooper()).postDelayed({
                    iv_poster_horizontal.stopLoading()
                }, minShimmerTime)

                //Set poster click listener
                cv_poster_horizontal.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, type)

                    context.startActivity(intent)
                }
            }
        }
    }


}