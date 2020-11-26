package com.dhimas.dhiflix.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dhimas.dhiflix.R
import com.dhimas.dhiflix.data.source.local.entity.ShowEntity
import com.dhimas.dhiflix.databinding.ItemBannerBinding
import com.dhimas.dhiflix.ui.detail.DetailActivity
import com.dhimas.dhiflix.utils.Const
import com.squareup.picasso.Picasso

class BannerAdapter(val context: Context) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {
    private var bannerEntities = ArrayList<ShowEntity>()

    fun addBanner(showEntity: ShowEntity) {
        this.bannerEntities.add(showEntity)
    }

    fun clearBanner() {
        this.bannerEntities.clear()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.bind(bannerEntities[position])
    }

    override fun getItemCount(): Int = bannerEntities.size

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemBannerBinding.bind(itemView)
        fun bind(showEntity: ShowEntity) {
            with(binding) {
                Picasso.get()
                    .load(Const.URL_BASE_IMAGE + showEntity.backdropPath)
                    .placeholder(R.drawable.backdrop_placeholder)
                    .error(R.drawable.image_error)
                    .into(ivBanner)

                tvBannerTitle.text = showEntity.title

                root.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)

                    intent.putExtra(DetailActivity.EXTRA_SHOW_ID, showEntity.id)
                    intent.putExtra(DetailActivity.EXTRA_SHOW_TYPE, showEntity.showType)

                    itemView.context.startActivity(intent)
                }
            }
        }
    }

}