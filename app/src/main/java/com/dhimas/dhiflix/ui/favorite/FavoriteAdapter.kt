package com.dhimas.dhiflix.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ItemShowHorizontalBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Const
import com.squareup.picasso.Picasso

class FavoriteAdapter internal constructor() :
    PagedListAdapter<ShowEntity, FavoriteAdapter.ShowViewHolder>(DIFF_CALLBACK) {
    private var listType = Const.MOVIE_TYPE //Default type is movie
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
            .inflate(R.layout.item_show_horizontal, parent, false)

        return ShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val showEntity = getItem(position)
        if (showEntity != null) {
            holder.bind(showEntity, listType, isAlreadyShimmer)
        }
    }

    class ShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowHorizontalBinding.bind(itemView)
        fun bind(showEntity: ShowEntity, type: Int, isAlreadyShimmer: Boolean) {
            with(binding) {

                //Start shimmer
                if (isAlreadyShimmer)
                    ivPosterHorizontal.startLoading()

                //Horizontal Poster
                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + showEntity.posterPath!!)
                    .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPosterHorizontal)

                //Stop shimmer
                ivPosterHorizontal.stopLoading()

                //Set poster click listener
                cvPosterHorizontal.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, type)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }


}