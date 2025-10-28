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
import kotlin.collections.set

class ShortsFollwoingAdapter(
    private val shortsViewModel: ShortsViewModel
) : RecyclerView.Adapter<ShortsFollwoingAdapter.ShortViewHolder>() {

    private var shorts: List<ShortVideo> = emptyList()

    private var likedShorts: Set<String> = emptySet()
    inner class ShortViewHolder(val binding: ItemShortVideoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortViewHolder {
        val binding : ItemShortVideoBinding = ItemShortVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false)

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
//            Toast.makeText(holder.itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
        }

        short.videoUrl?.let {

            val videoUrl = it ?: return
            if (isYouTubeUrl(videoUrl)) {
                holder.binding.youtubePlayerView.visibility = View.GONE
                holder.binding.youtubePlayerView.visibility = View.VISIBLE
                Log.d("TAG_post", "onBindViewHolder: ${position} && ${short.title}")
                setupYouTubePlayer(holder, videoUrl)
            } else {
                holder.binding.youtubePlayerView.visibility = View.GONE
                holder.binding.playerView.visibility = View.VISIBLE
            }
        }

    }


    private fun isYouTubeUrl(url: String): Boolean {
        return url.contains("youtube.com") || url.contains("youtu.be")
    }


    private fun setupYouTubePlayer(holder: ShortViewHolder, url: String) {
        val videoId = extractYoutubeVideoId(url)

        if (holder.binding.youtubePlayerView.tag == null) {
            holder.binding.youtubePlayerView.tag = true // mark initialized once

            holder.binding.youtubePlayerView.enableAutomaticInitialization = false

            val options = IFramePlayerOptions.Builder(holder.binding.root.context)
                .controls(0)
                .rel(0)
                .build()

            holder.binding.youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youTubePlayer.loadVideo(videoId, 0f)
                    activePlayers[holder.adapterPosition] = youTubePlayer

                    youTubePlayer.cueVideo(videoId, 0f)

                }

                override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
                    super.onError(youTubePlayer, error)
                    Toast.makeText(holder.itemView.context, "YouTube error: $error", Toast.LENGTH_SHORT).show()
                }
            }, options)
        } else {
            // If already initialized, reuse the existing player
            holder.binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback{
                override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
//                    youTubePlayer.loadVideo(videoId, 0f)
                    activePlayers[holder.adapterPosition] = youTubePlayer

                    youTubePlayer.cueVideo(videoId, 0f)

                }

            })
        }
    }



    private fun extractYoutubeVideoId(url: String): String {
        // Works for normal YouTube & Shorts URLs
        return when {
            url.contains("shorts/") -> url.substringAfter("shorts/").substringBefore("?")
            url.contains("v=") -> url.substringAfter("v=").substringBefore("&")
            url.contains("youtu.be/") -> url.substringAfter("youtu.be/").substringBefore("?")
            else -> ""
        }
    }



    override fun getItemCount(): Int = shorts.size

    fun submitList(newShorts: List<ShortVideo>) {
        shorts = newShorts
        notifyDataSetChanged()
    }



    private val activePlayers = mutableMapOf<Int, YouTubePlayer>()

    fun playVideoAt(position: Int) {
        if (shorts.isEmpty() || position !in shorts.indices) {
            Log.w("ShortsPagerAdapter", "⚠️ playVideoAt: invalid index $position for list size ${shorts.size}")
            return
        }

        activePlayers[position]?.loadVideo(extractYoutubeVideoId(shorts[position].videoUrl ?: ""), 0f)
    }

    fun pauseVideoAt(position: Int) {
        activePlayers[position]?.pause()
    }

    override fun onViewDetachedFromWindow(holder: ShortViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.binding.youtubePlayerView.getYouTubePlayerWhenReady(object : YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                youTubePlayer.pause()

            }

        })
    }


    fun updateLikedVideos(newLikedSet: Set<String>) {
        likedShorts = newLikedSet
        notifyDataSetChanged()
    }


}