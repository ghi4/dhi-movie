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
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class FavoriteAdapter internal constructor() :
    PagedListAdapter<ShowEntity, FavoriteAdapter.ShowViewHolder>(DIFF_CALLBACK) {
    private var listType = DetailActivity.EXTRA_FROM_MOVIES //Default type is movie
    private var isAlreadyShimmer: Boolean = false

    fun setMovies(listType: String, isAlreadyShimmer: Boolean) {
        this.listType = listType
        this.isAlreadyShimmer = isAlreadyShimmer
    }

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
        fun bind(showEntity: ShowEntity, type: String, isAlreadyShimmer: Boolean) {
            with(itemView) {
                iv_poster_horizontal.startLoading()

                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + showEntity.posterPath!!)
                    .resize(Constant.POSTER_TARGET_WIDTH, Constant.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.image_error_2_3)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(iv_poster_horizontal)

                //Prevent re-shimmer when scrolling view
                if (!isAlreadyShimmer) {
                    //If data loaded too fast causing awkward animation/view
                    Handler(Looper.getMainLooper()).postDelayed({
                        iv_poster_horizontal.stopLoading()
                    }, Constant.MINIMUM_SHIMMER_TIME)
                } else {
                    Handler(Looper.getMainLooper()).postDelayed({
                        iv_poster_horizontal.stopLoading()
                    }, Constant.MINIMUM_SHIMMER_TIME / 10)
                }

                cv_poster_horizontal.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)

                    //Used for checking the show title type is movie or series
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, type)

                    context.startActivity(intent)
                }
            }
        }
    }


}