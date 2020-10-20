package com.dhimas.dhiflix.ui.movie

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.ShowEntity
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private var listMovies = ArrayList<ShowEntity>()

    fun setMovies(movies: ArrayList<ShowEntity>) {
        this.listMovies.clear()
        this.listMovies.addAll(movies)
    }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movieEntity = listMovies[position]
        holder.bind(movieEntity)
    }

    override fun getItemCount(): Int = listMovies.size

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: ShowEntity) {
            with(itemView) {
                tv_title.text = movie.title
                tv_release_year.text = movie.releaseYear

                val posterTargetWidth = 200
                val posterTargetHeight = 300

                Picasso.get()
                        .load(movie.posterPath!!)
                        .resize(posterTargetWidth, posterTargetHeight)
                        .error(R.drawable.image_error_2_3)
                        .placeholder(R.drawable.placeholder_2_3)
                        .into(iv_poster)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TITLE, movie.title)

                    //Used for checking if the show entity is from movie page
                    //Sending empty value because I use key for checking without read the data
                    intent.putExtra(DetailActivity.EXTRA_FROM_MOVIES, "")

                    context.startActivity(intent)
                }
            }
        }
    }

}