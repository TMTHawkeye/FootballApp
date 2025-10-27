package com.example.footballapp.adapters.highlightadapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapp.R
import com.example.footballapp.VideoPlayerActivity

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    private var videos: List<String> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewVideoThumb)

        fun bind(videoUrl: String) {
            // Extract YouTube video ID
            val videoId = extractYouTubeId(videoUrl)

            // Construct thumbnail URL
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/hqdefault.jpg"

            // Load thumbnail using Glide
            Glide.with(itemView.context)
                .load(thumbnailUrl)
                 .into(imageView)

            // Example: Set dummy title/duration if you want later
            // titleTextView.text = "Video ID: $videoId"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, VideoPlayerActivity::class.java).apply {
                    putExtra("video_url", videoUrl)
//                    putExtra("current_position", adapterPosition)
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(videos[position])
    }

    override fun getItemCount(): Int = videos.size

    fun submitList(newVideos: List<String>) {
        videos = newVideos
        notifyDataSetChanged()
    }

    // 🔹 Helper function to extract YouTube video ID
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
