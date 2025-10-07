package com.example.footballapp.adapters.highlightadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapp.R

class HighlightsPagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<HighlightsPagerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivSliderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageRes = images[position]
        Glide.with(holder.itemView.context)
            .load(imageRes)
            .centerCrop()
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size
}