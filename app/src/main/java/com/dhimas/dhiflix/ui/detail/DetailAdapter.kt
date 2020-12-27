package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.domain.model.Show
import com.dhimas.dhiflix.databinding.ItemShowHorizontalBinding
import com.dhimas.dhiflix.utils.Const
import com.squareup.picasso.Picasso

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var isAlreadyShimmer: Boolean = false
    private var showList = ArrayList<Show>()

    fun setList(showList: ArrayList<Show>) {
        this.showList.clear()
        this.showList.addAll(showList)
    }

    fun setShimmer(isAlreadyShimmer: Boolean) {
        this.isAlreadyShimmer = isAlreadyShimmer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_show_horizontal, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val movie = showList[position]
        holder.bind(movie, isAlreadyShimmer)
    }

    override fun getItemCount(): Int = showList.size

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowHorizontalBinding.bind(itemView)
        fun bind(show: Show, isAlreadyShimmer: Boolean) {
            with(binding) {

                //Start shimmer
                if (!isAlreadyShimmer)
                    ivPosterHorizontal.startLoading()

                //Horizontal Poster
                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + show.posterPath)
                    .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPosterHorizontal)

                //Stop shimmer
                ivPosterHorizontal.stopLoading()

                //Set poster click listener
                cvPosterHorizontal.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, show.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, show.showType)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}