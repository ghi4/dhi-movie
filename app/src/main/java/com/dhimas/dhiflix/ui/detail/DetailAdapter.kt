package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ItemShowHorizontalBinding
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.Utils.getMinShimmerTime
import com.squareup.picasso.Picasso

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var isAlreadyShimmer: Boolean = false
    private var showList = ArrayList<ShowEntity>()

    fun setList(showList: ArrayList<ShowEntity>) {
        showList.clear()
        showList.addAll(showList)
    }

    fun setMovies(isAlreadyShimmer: Boolean) {
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
        fun bind(showEntity: ShowEntity, isAlreadyShimmer: Boolean) {
            with(binding) {

                //Start shimmer
                ivPosterHorizontal.startLoading()

                //Horizontal Poster
                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + showEntity.posterPath)
                    .resize(Constant.POSTER_TARGET_WIDTH, Constant.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPosterHorizontal)

                //Delay for shimmer animation
                val minShimmerTime = getMinShimmerTime(isAlreadyShimmer)
                Handler(Looper.getMainLooper()).postDelayed({
                    ivPosterHorizontal.stopLoading()
                }, minShimmerTime)

                //Set poster click listener
                cvPosterHorizontal.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, showEntity.showType)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}