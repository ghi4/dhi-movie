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
import com.dhimas.dhiflix.utils.Constant
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie_horizontal.view.*

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var listMovies = ArrayList<ShowEntity>()
    private var listType = DetailActivity.EXTRA_FROM_MOVIES //Default type is movie
    private var isAlreadyShimmer: Boolean = false

    fun setMovies(listShows: ArrayList<ShowEntity>, listType: String, isAlreadyShimmer: Boolean) {
        this.listMovies.clear()
        this.listMovies.addAll(listShows)
        this.listType = listType
        this.isAlreadyShimmer = isAlreadyShimmer
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie_horizontal, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val movie = listMovies[position]
        holder.bind(movie, listType, isAlreadyShimmer)
    }

    override fun getItemCount(): Int = listMovies.size

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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