package com.dhimas.dhiflix.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.core.presenter.model.ShowsModel
import com.dhimas.dhiflix.core.utils.Const
import com.dhimas.dhiflix.databinding.ItemBannerBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.squareup.picasso.Picasso

class BannerAdapter(val context: Context) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private var bannerList = ArrayList<ShowsModel>()

    fun setBanner(shows: ArrayList<ShowsModel>) {
        val bannerCount = 5
        bannerList.clear()
        bannerList.addAll(shows.take(bannerCount))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerList[position])
    }

    override fun getItemCount(): Int = bannerList.size

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBannerBinding.bind(itemView)
        fun bind(show: ShowsModel) {
            with(binding) {
                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + show.backdropPath)
                    .placeholder(R.drawable.backdrop_placeholder)
                    .error(R.drawable.image_error)
                    .into(ivBanner)

                tvBannerTitle.text = show.title

                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, show.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, show.showType)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}