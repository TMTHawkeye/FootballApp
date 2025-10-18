package com.example.footballapp.adapters.newsadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.R

class SliderAdapter(private val items: MutableList<LatestNewsResponseItem>) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivSliderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.imageView.context)
            .load(item.image)
            .into(holder.imageView)
    }

    fun updateList(newList : List<LatestNewsResponseItem>){
        items.clear()
        items?.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}