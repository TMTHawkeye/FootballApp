package com.example.footballapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.databinding.ActivityVideoPlayerBinding
import com.example.footballapp.models.highlightmodel.Video
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var videoList: List<Video>
    private var currentPosition = 0
    private lateinit var videoPlayerAdapter: VideoPlayerAdapter
    private var exoPlayer: SimpleExoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false // For light icons (use with dark backgrounds)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get data from intent
        videoList = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("video_list", Video::class.java) ?: emptyList()
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayListExtra("video_list") ?: emptyList()
        }
        currentPosition = intent.getIntExtra("current_position", 0)

        setupViewPager()
    }

    private fun setupViewPager() {
        videoPlayerAdapter = VideoPlayerAdapter(videoList)
        binding.viewPagerVideos.adapter = videoPlayerAdapter
        binding.viewPagerVideos.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPagerVideos.setCurrentItem(currentPosition, false)

        // Handle page changes to release previous player
        binding.viewPagerVideos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                releasePlayer()
            }
        })
    }

    private fun releasePlayer() {
        exoPlayer?.stop()
        exoPlayer?.release()
        exoPlayer = null
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        videoPlayerAdapter.releaseAllPlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
        videoPlayerAdapter.releaseAllPlayers()
    }

    inner class VideoPlayerAdapter(private val videos: List<Video>) :
        RecyclerView.Adapter<VideoPlayerAdapter.VideoPlayerViewHolder>() {

        private val viewHolders = mutableListOf<VideoPlayerViewHolder>()
        private val handler = Handler(Looper.getMainLooper())

        inner class VideoPlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val playerView: PlayerView = itemView.findViewById(R.id.playerView)
            val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
            val playPauseButton: ImageView = itemView.findViewById(R.id.btnPlayPause)
            val videoTitle: TextView = itemView.findViewById(R.id.tvVideoTitle)
            var player: SimpleExoPlayer? = null
            private var hideControlsRunnable: Runnable? = null

            fun bind(video: Video) {
                videoTitle.text = video.title

                // Initialize ExoPlayer
                player = SimpleExoPlayer.Builder(itemView.context).build()
                playerView.player = player

                // Set media item
                val mediaItem = MediaItem.fromUri(Uri.parse(video.videoUrl))
                player?.setMediaItem(mediaItem)
                player?.prepare()

                // Show controls initially
                showControls()

                // Play video automatically when ready
                player?.addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(state: Int) {
                        when (state) {
                            Player.STATE_READY -> {
                                progressBar.visibility = View.GONE
                                player?.play()
                                // Start the timer to hide controls after 2 seconds
                                startHideControlsTimer()
                            }
                            Player.STATE_BUFFERING -> {
                                progressBar.visibility = View.VISIBLE
                            }
                        }
                    }
                })

                // Play/Pause button click listener
                playPauseButton.setOnClickListener {
                    if (player?.isPlaying == true) {
                        player?.pause()
                        playPauseButton.setImageResource(R.drawable.play)
                    } else {
                        player?.play()
                        playPauseButton.setImageResource(R.drawable.pause)
                    }
                    // Reset the hide timer when user interacts
                    resetHideControlsTimer()
                }

                // Hide/show controls when video is tapped
                playerView.setOnClickListener {
                    if (playPauseButton.visibility == View.VISIBLE) {
                        hideControls()
                    } else {
                        showControls()
                        // Start timer to hide again after 2 seconds
                        startHideControlsTimer()
                    }
                }
            }

            private fun showControls() {
                playPauseButton.visibility = View.VISIBLE
                videoTitle.visibility = View.VISIBLE
            }

            private fun hideControls() {
                playPauseButton.visibility = View.GONE
                videoTitle.visibility = View.GONE
            }

            private fun startHideControlsTimer() {
                // Remove any existing runnable
                hideControlsRunnable?.let { handler.removeCallbacks(it) }

                // Create new runnable to hide controls after 2 seconds
                hideControlsRunnable = Runnable {
                    hideControls()
                }
                handler.postDelayed(hideControlsRunnable!!, 2000)
            }

            private fun resetHideControlsTimer() {
                // Remove existing timer and start a new one
                startHideControlsTimer()
            }

            fun releasePlayer() {
                // Remove any pending hide runnables
                hideControlsRunnable?.let { handler.removeCallbacks(it) }
                hideControlsRunnable = null

                player?.stop()
                player?.release()
                player = null
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoPlayerViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_fullscreen_video, parent, false)
            return VideoPlayerViewHolder(view).also {
                viewHolders.add(it)
            }
        }

        override fun onBindViewHolder(holder: VideoPlayerViewHolder, position: Int) {
            holder.bind(videos[position])
        }

        override fun getItemCount(): Int = videos.size

        override fun onViewRecycled(holder: VideoPlayerViewHolder) {
            super.onViewRecycled(holder)
            holder.releasePlayer()
        }

        fun releaseAllPlayers() {
            viewHolders.forEach { it.releasePlayer() }
            viewHolders.clear()
        }
    }
}