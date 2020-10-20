package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.ShowEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var listMovies = ArrayList<ShowEntity>()
    private var listType = DetailActivity.EXTRA_FROM_MOVIES //Default type is movie

    fun setMovies(listShows: ArrayList<ShowEntity>, listType: String) {
        this.listMovies.clear()
        this.listMovies.addAll(listShows)
        this.listType = listType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie_horizontal, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val movie = listMovies[position]
        holder.bind(movie, listType)
    }

    override fun getItemCount(): Int = listMovies.size

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(showEntity: ShowEntity, type: String) {
            with(itemView) {
                val posterTargetWidth = 200
                val posterTargetHeight = 300

                Picasso.get()
                        .load(showEntity.posterPath!!)
                        .resize(posterTargetWidth, posterTargetHeight)
                        .error(R.drawable.image_error_2_3)
                        .placeholder(R.drawable.placeholder_2_3)
                        .into(iv_poster_horizontal)

                cv_poster_horizontal.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TITLE, showEntity.title)

                    //Used for checking the show title type is movie or series
                    //Sending empty message because I use key for checking without read the data
                    intent.putExtra(type, "")

                    context.startActivity(intent)
                }
            }
        }
    }
}