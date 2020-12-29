package com.dhimas.dhiflix.core.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.core.R
import com.dhimas.dhiflix.core.databinding.ItemShowBinding
import com.dhimas.dhiflix.core.presenter.model.ShowsModel
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.core.utils.Utils
import com.squareup.picasso.Picasso

class ShowsAdapter : RecyclerView.Adapter<ShowsAdapter.MovieViewHolder>() {
    private var showsList = ArrayList<ShowsModel>()
    var onItemClick: ((ShowsModel) -> Unit)? = null

    fun setList(shows: ArrayList<ShowsModel>) {
        showsList.clear()
        showsList.addAll(shows)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(showsList[position])
    }

    override fun getItemCount(): Int = showsList.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowBinding.bind(itemView)
        fun bind(show: ShowsModel) {
            with(binding) {
                //Title
                tvTitle.text = show.title
                //Release date
                tvReleaseDate.text = Utils.dateParseToMonthAndYear(show.releaseDate)
                //Poster
                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + show.posterPath)
                    .resize(Const.POSTER_TARGET_WIDTH, Const.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPoster)
            }
        }

        init {
            binding.root.setOnClickListener {
                onItemClick?.invoke(showsList[adapterPosition])
            }
        }
    }
}