package com.example.footballapp.adapters.shortsadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemShortVideoBinding
import com.example.footballapp.models.shortsmodel.ShortVideo
import com.example.footballapp.viewmodels.ShortsViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions

class ShortsPagerAdapter(
    private val shortsViewModel: ShortsViewModel
) : RecyclerView.Adapter<ShortsPagerAdapter.ShortViewHolder>() {

    private var shorts: List<ShortVideo> = emptyList()
    private var likedShorts: Set<String> = emptySet()
    private val activePlayers = mutableMapOf<Int, YouTubePlayer>()

    inner class ShortViewHolder(val binding: ItemShortVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortViewHolder {
        val binding = ItemShortVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShortViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShortViewHolder, position: Int) {
        val short = shorts[position]
        val isLiked = likedShorts.contains(short.videoUrl)

        // ✅ Update like button icon
        holder.binding.btnLike.setImageResource(
            if (isLiked) R.drawable.likee else R.drawable.unlike
        )

        holder.binding.btnLike.setOnClickListener {
            shortsViewModel.toggleLike(short.videoUrl ?: return@setOnClickListener)
//            Toast.makeText(
//                holder.itemView.context,
//                if (isLiked) "Unliked!" else "Liked!",
//                Toast.LENGTH_SHORT
//            ).show()
        }

        // ✅ Handle YouTube videos
        short.videoUrl?.let { videoUrl ->
            if (isYouTubeUrl(videoUrl)) {
                holder.binding.youtubePlayerView.visibility = View.VISIBLE
                holder.binding.playerView.visibility = View.GONE
                setupYouTubePlayer(holder, videoUrl)
            } else {
                holder.binding.youtubePlayerView.visibility = View.GONE
                holder.binding.playerView.visibility = View.VISIBLE
            }
        }
    }

    /** 🔁 Update the adapter when liked set changes */
    fun updateLikedVideos(newLikedSet: Set<String>) {
        likedShorts = newLikedSet
        notifyDataSetChanged()
    }

    /** ✅ Handle YouTube setup */
    private fun setupYouTubePlayer(holder: ShortViewHolder, url: String) {
        val videoId = extractYoutubeVideoId(url)

        if (holder.binding.youtubePlayerView.tag == null) {
            holder.binding.youtubePlayerView.tag = true
            holder.binding.youtubePlayerView.enableAutomaticInitialization = false

            val options = IFramePlayerOptions.Builder(holder.binding.root.context)
                .controls(0)
                .rel(0)
                .build()

            holder.binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    activePlayers[holder.adapterPosition] = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)
                }

                override fun onError(
                    youTubePlayer: YouTubePlayer,
                    error: PlayerConstants.PlayerError
                ) {
                    super.onError(youTubePlayer, error)
                    Toast.makeText(holder.itemView.context, "YouTube error: $error", Toast.LENGTH_SHORT).show()
                }
            }, options)
        } else {
            holder.binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                    activePlayers[holder.adapterPosition] = youTubePlayer
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    }

    private fun extractYoutubeVideoId(url: String): String {
        return when {
            url.contains("shorts/") -> url.substringAfter("shorts/").substringBefore("?")
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
            else -> ""
        }
    }

    private fun isYouTubeUrl(url: String): Boolean {
        return url.contains("youtube.com") || url.contains("youtu.be")
    }

    override fun getItemCount(): Int = shorts.size

    fun submitList(newShorts: List<ShortVideo>) {
        shorts = newShorts
        notifyDataSetChanged()
    }

    fun playVideoAt(position: Int) {
        if (position !in shorts.indices) return
        activePlayers[position]?.loadVideo(extractYoutubeVideoId(shorts[position].videoUrl ?: ""), 0f)
    }

    fun pauseVideoAt(position: Int) {
        activePlayers[position]?.pause()
    }

    override fun onViewDetachedFromWindow(holder: ShortViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.pause()
            }
        })
    }
}
