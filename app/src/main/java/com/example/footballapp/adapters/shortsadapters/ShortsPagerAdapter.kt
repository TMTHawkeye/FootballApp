package com.example.footballapp.adapters.shortsadapters

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.models.shortsmodel.ShortVideo
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class ShortsPagerAdapter(
    fragment: Fragment
) : RecyclerView.Adapter<ShortsPagerAdapter.ShortViewHolder>() {

    private val fragment = fragment
    private var shorts: List<ShortVideo> = emptyList()
    private val players = mutableMapOf<Int, ExoPlayer>()
    private val handlers = mutableMapOf<Int, Handler>()
    private val hideDelay = 2000L // 2 seconds delay
    private var currentPlayingPosition = -1 // Track which position is currently playing
    private var isFirstPlayTriggered = false

    inner class ShortViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerView: PlayerView = itemView.findViewById(R.id.playerView)
        val titleText: TextView = itemView.findViewById(R.id.tvTitle)
        val descriptionText: TextView = itemView.findViewById(R.id.tvDescription)
        val likeButton: ImageView = itemView.findViewById(R.id.btnLike)
        val shareButton: ImageView = itemView.findViewById(R.id.btnShare)
        val likesText: TextView = itemView.findViewById(R.id.tvLikesCount)
        val playPauseButton: ImageView = itemView.findViewById(R.id.btnPlayPause)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_short_video, parent, false)
        return ShortViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShortViewHolder, position: Int) {
        val short = shorts[position]

        // Set video info
        holder.titleText.text = short.title
        holder.descriptionText.text = short.description
        holder.likesText.text = formatLikes(short.likes)

        // Set like button state
        holder.likeButton.setImageResource(
            if (short.isLiked) R.drawable.likee else R.drawable.likee
        )

        // Like button click
        holder.likeButton.setOnClickListener {
            // Handle like logic here
            Toast.makeText(holder.itemView.context, "Liked!", Toast.LENGTH_SHORT).show()
        }

        // Share button click
        holder.shareButton.setOnClickListener {
            // Handle share logic here
            Toast.makeText(holder.itemView.context, "Sharing...", Toast.LENGTH_SHORT).show()
        }

        // Setup touch listener for the entire view to show/hide controls
        holder.itemView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                toggleControlsVisibility(holder, position)
            }
            true
        }

        // Setup ExoPlayer for video
        setupPlayer(holder, short.videoUrl, position)

        // Play/Pause button click listener
        holder.playPauseButton.setOnClickListener {
            togglePlayPause(holder, position)
            // Reset the hide timer after user interaction
            resetHideTimer(holder, position)
        }

        // Set initial play/pause button icon based on whether this is the current playing position
        val isPlaying = position == currentPlayingPosition
        updatePlayPauseButton(holder, isPlaying)

        if (isPlaying) {
            hideControls(holder) // Hide controls if video is playing
        } else {
            showControls(holder) // Show controls if video is paused
        }
    }

    private fun setupPlayer(holder: ShortViewHolder, videoUrl: String, position: Int) {
        // Release previous player for this position if exists
        players[position]?.release()
        handlers[position]?.removeCallbacksAndMessages(null)

        val player = ExoPlayer.Builder(holder.itemView.context).build()
        players[position] = player
        holder.playerView.player = player

        val handler = Handler(Looper.getMainLooper())
        handlers[position] = handler

        val mediaItem = MediaItem.fromUri(videoUrl)
        player.setMediaItem(mediaItem)
        player.prepare()

        // Don't autoplay by default - we'll handle this in onPageChanged
        player.playWhenReady = (position == currentPlayingPosition)

        // Add listener to update play/pause button state
        player.addListener(object : com.google.android.exoplayer2.Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
                val isPlaying = state == com.google.android.exoplayer2.Player.STATE_READY && player.playWhenReady
                updatePlayPauseButton(holder, isPlaying)

                // Start hide timer when video starts playing
                if (isPlaying) {
                    startHideTimer(holder, position)
                }
            }
        })
    }

    private fun togglePlayPause(holder: ShortViewHolder, position: Int) {
        val player = players[position] ?: return
        val isPlaying = player.playWhenReady

        if (isPlaying) {
            // Pause this video
            player.playWhenReady = false
            updatePlayPauseButton(holder, false)
            showControls(holder)
            if (currentPlayingPosition == position) {
                currentPlayingPosition = -1
            }
        } else {
            // ✅ If first play → mark it triggered
            if (!isFirstPlayTriggered) {
                isFirstPlayTriggered = true
            }

            // Play this video and pause any others
            if (currentPlayingPosition != -1 && currentPlayingPosition != position) {
                players[currentPlayingPosition]?.playWhenReady = false
            }

            player.playWhenReady = true
            updatePlayPauseButton(holder, true)
            startHideTimer(holder, position)
            currentPlayingPosition = position
        }
    }


    private fun updatePlayPauseButton(holder: ShortViewHolder, isPlaying: Boolean) {
        if (isPlaying) {
            holder.playPauseButton.setImageResource(R.drawable.pause)
        } else {
            holder.playPauseButton.setImageResource(R.drawable.play)
        }
    }

    private fun toggleControlsVisibility(holder: ShortViewHolder, position: Int) {
        if (holder.playPauseButton.visibility == View.VISIBLE) {
            hideControls(holder)
        } else {
            showControls(holder)
            // Reset the hide timer when controls are shown
            resetHideTimer(holder, position)
        }
    }

    private fun showControls(holder: ShortViewHolder) {
        holder.playPauseButton.visibility = View.VISIBLE
        // Show other controls if needed
    }

    private fun hideControls(holder: ShortViewHolder) {
        holder.playPauseButton.visibility = View.INVISIBLE
        // Hide other controls if needed
    }

    private fun startHideTimer(holder: ShortViewHolder, position: Int) {
        handlers[position]?.removeCallbacksAndMessages(null)
        handlers[position]?.postDelayed({
            hideControls(holder)
        }, hideDelay)
    }

    private fun resetHideTimer(holder: ShortViewHolder, position: Int) {
        val player = players[position]
        if (player != null && player.playWhenReady) {
            startHideTimer(holder, position)
        }
    }

    private fun formatLikes(likes: Int): String {
        return when {
            likes >= 1000000 -> "${likes / 1000000}M"
            likes >= 1000 -> "${likes / 1000}K"
            else -> likes.toString()
        }
    }

    override fun getItemCount(): Int = shorts.size

    fun submitList(newShorts: List<ShortVideo>) {
        shorts = newShorts
        notifyDataSetChanged()
    }

    // Add this method to handle view pager page changes
    fun onPageChanged(newPosition: Int) {
        if (currentPlayingPosition == newPosition) return

        // Pause previous
        if (currentPlayingPosition != -1) {
            players[currentPlayingPosition]?.playWhenReady = false
        }

        // ✅ Only auto-play if first play was triggered
        if (isFirstPlayTriggered) {
            players[newPosition]?.playWhenReady = true
            currentPlayingPosition = newPosition

            handlers[newPosition]?.postDelayed({
                hideControlsForPosition(newPosition)
            }, hideDelay)
        } else {
            currentPlayingPosition = -1
        }
    }


    private fun hideControlsForPosition(position: Int) {
        // This would need a reference to the view holder, but we can use notify
        notifyItemChanged(position)
    }

    fun pauseCurrentVideo() {
        if (currentPlayingPosition != -1) {
            players[currentPlayingPosition]?.playWhenReady = false
        }
        currentPlayingPosition = -1
    }

    fun resumeCurrentVideo() {
        // Don't auto-resume, let the page change handle it
    }

    fun releasePlayer() {
        players.values.forEach { it.release() }
        players.clear()
        handlers.values.forEach { it.removeCallbacksAndMessages(null) }
        handlers.clear()
        currentPlayingPosition = -1
    }

    override fun onViewRecycled(holder: ShortViewHolder) {
        super.onViewRecycled(holder)
        val position = holder.adapterPosition
        if (position != RecyclerView.NO_POSITION) {
            players[position]?.release()
            players.remove(position)
            handlers[position]?.removeCallbacksAndMessages(null)
            handlers.remove(position)

            // If the recycled view was the currently playing one, reset
            if (position == currentPlayingPosition) {
                currentPlayingPosition = -1
            }
        }
    }
}