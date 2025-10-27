package com.example.footballapp.adapters.highlightadapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapp.R
import com.example.footballapp.VideoPlayerActivity

class HighlightsPagerAdapter(private val images: List<String>) : RecyclerView.Adapter<HighlightsPagerAdapter.ViewHolder>() {

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
        val videoId = extractYouTubeId(imageRes)
        val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

        Glide.with(holder.itemView.context)
            .load(thumbnailUrl)
             .into(holder.imageView)

        holder.itemView.rootView.setOnClickListener {
            val intent = Intent(holder.itemView.context, VideoPlayerActivity::class.java).apply {
                putExtra("video_url", imageRes)
//                    putExtra("current_position", adapterPosition)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = images.size

    private fun extractYouTubeId(url: String): String? {
        val regex = "(?<=v=)[^#&?]*".toRegex()
        val match = regex.find(url)
        return match?.value ?: run {
            // Try alternate formats like youtu.be links
            val shortRegex = "(?<=be/)[^#&?]*".toRegex()
            shortRegex.find(url)?.value
        }
    }
}