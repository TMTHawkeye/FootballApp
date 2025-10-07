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

import com.example.footballapp.models.highlightmodel.Video

class VideosAdapter : RecyclerView.Adapter<VideosAdapter.ViewHolder>() {

    private var videos: List<Video> = emptyList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewVideoThumb)
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewVideoTitle)
        private val durationTextView: TextView = itemView.findViewById(R.id.textViewVideoDuration)
        private val playButton: ImageView = itemView.findViewById(R.id.imageViewPlayButton)

        fun bind(video: Video) {
            Glide.with(itemView.context)
                .load(video.thumbnailRes)
                .centerCrop()
                .into(imageView)
            titleTextView.text = video.title
            durationTextView.text = video.duration

            itemView.setOnClickListener {
                // Launch video player activity
                val intent = Intent(itemView.context, VideoPlayerActivity::class.java).apply {
                    putExtra("video_url", video.videoUrl)
                    putExtra("video_title", video.title)
                    putParcelableArrayListExtra("video_list", ArrayList(videos))
                    putExtra("current_position", adapterPosition)
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

    fun submitList(newVideos: List<Video>) {
        videos = newVideos
        notifyDataSetChanged()
    }
}