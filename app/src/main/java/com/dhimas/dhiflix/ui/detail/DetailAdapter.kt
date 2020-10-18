package com.dhimas.dhiflix.ui.detail

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.MovieEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class DetailAdapter: RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var listMovies = ArrayList<MovieEntity>()

    fun setMovies(listMovies: ArrayList<MovieEntity>){
        this.listMovies.clear()
        this.listMovies.addAll(listMovies)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_movie_horizontal, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val movie = listMovies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = listMovies.size

    class DetailViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(movieEntity: MovieEntity){
            with(itemView){
                Picasso.get()
                    .load(movieEntity.posterPath!!)
                    .resize(200, 300)
                    .error(R.drawable.image_error_2_3)
                    .placeholder(R.drawable.placeholder_2_3)
                    .into(iv_poster_horizontal)

                cv_poster_horizontal.setOnClickListener{
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("movie_key", movieEntity)
                    context.startActivity(intent)
                }
            }
        }
    }
}