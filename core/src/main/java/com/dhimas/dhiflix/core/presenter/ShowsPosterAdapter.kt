package com.dhimas.dhiflix.core.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.core.R
import com.dhimas.dhiflix.core.databinding.ItemShowHorizontalBinding
import com.dhimas.dhiflix.core.presenter.model.ShowsPosterModel
import com.dhimas.dhiflix.core.utils.Const

class ShowsPosterAdapter : RecyclerView.Adapter<ShowsPosterAdapter.DetailViewHolder>() {
    private var isAlreadyShimmer: Boolean = false
    private var showList = ArrayList<ShowsPosterModel>()
    var onItemClick: ((ShowsPosterModel) -> Unit)? = null

    fun setList(shows: ArrayList<ShowsPosterModel>) {
        showList.clear()
        showList.addAll(shows)
        notifyDataSetChanged()
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
        val show = showList[position]
        holder.bind(show, isAlreadyShimmer)
    }

    override fun getItemCount(): Int = showList.size

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowHorizontalBinding.bind(itemView)
        fun bind(show: ShowsPosterModel, isAlreadyShimmer: Boolean) {
            with(binding) {

                //Start shimmer
                if (!isAlreadyShimmer)
                    ivPosterHorizontal.startLoading()

                //Horizontal Poster
                com.squareup.picasso.Picasso.get()
                    .load(Const.URL_BASE_IMAGE + show.posterPath)
                    .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPosterHorizontal)

                //Stop shimmer
                ivPosterHorizontal.stopLoading()
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(showList[adapterPosition])
            }
        }
    }
}