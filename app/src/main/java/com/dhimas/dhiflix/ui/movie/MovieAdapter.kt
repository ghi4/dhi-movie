package com.dhimas.dhiflix.ui.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ItemShowBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Constant
import com.dhimas.dhiflix.utils.Utils
import com.squareup.picasso.Picasso

class MovieAdapter: RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var movieList = ArrayList<ShowEntity>()

    fun addMovies(movies: ArrayList<ShowEntity>) {
        movieList.clear()
        movieList.addAll(movies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    inner class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemShowBinding.bind(itemView)
        fun bind(movie: ShowEntity) {
            with(binding) {
                tvTitle.text = movie.title
                tvReleaseDate.text = Utils.dateParseToMonthAndYear(movie.releaseDate)

                Picasso.get()
                    .load(Constant.URL_BASE_IMAGE + movie.posterPath)
                    .resize(Constant.POSTER_TARGET_WIDTH, Constant.POSTER_TARGET_HEIGHT)
                    .error(R.drawable.poster_error)
                    .placeholder(R.drawable.poster_placeholder)
                    .into(ivPoster)

                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, movie.id)
                    //Used for checking if the show entity is from movie page
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, movie.showType)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}