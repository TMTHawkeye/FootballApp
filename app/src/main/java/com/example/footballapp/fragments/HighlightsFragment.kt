package com.example.footballapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.models.highlightmodel.Video // Add this import
import com.example.footballapp.adapters.highlightadapter.HighlightsPagerAdapter
import com.example.footballapp.adapters.highlightadapter.TabAdapter
import com.example.footballapp.adapters.highlightadapter.VideosAdapter
import com.example.footballapp.databinding.FragmentHighlightsBinding
import com.example.footballapp.models.highlightmodel.TabItem


class HighlightsFragment : Fragment() {

    private lateinit var binding: FragmentHighlightsBinding
    private lateinit var highlightsAdapter: HighlightsPagerAdapter
    private lateinit var videosAdapter: VideosAdapter
    private lateinit var tabAdapter: TabAdapter
    private val handler = Handler(Looper.getMainLooper())
    private  var autoSlideRunnable: Runnable?=null

    // Tab data
    private val tabs = listOf(
        TabItem("foryou", "For You", true),
        TabItem("trending", "Most Trending"),
        TabItem("manutd", "Manchester United"),
        TabItem("premier", "Premier League"),
        TabItem("champions", "Champions League")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHighlightsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupHighlightsSlider()
        setupTabRecyclerView()
        setupRecyclerView()
    }

    private fun setupHighlightsSlider() {
        val highlightImages = listOf(
            R.drawable.foot1,
            R.drawable.foot1,
            R.drawable.foot1,
            R.drawable.foot1,
            R.drawable.foot1
        )

        highlightsAdapter = HighlightsPagerAdapter(highlightImages)
        binding.viewPagerHighlights.adapter = highlightsAdapter

        // Set up CircleIndicator
        binding.indicator.setViewPager(binding.viewPagerHighlights)

        // Auto-slide functionality
        autoSlideRunnable = object : Runnable {
            override fun run() {
                val currentItem = binding.viewPagerHighlights.currentItem
                val nextItem = if (currentItem == highlightImages.size - 1) 0 else currentItem + 1
                binding.viewPagerHighlights.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 3000)
            }
        }
        autoSlideRunnable?.let { handler.postDelayed(it, 3000) }
    }

    private fun setupTabRecyclerView() {
        tabAdapter = TabAdapter(tabs) { selectedTab ->
            // Update selection
            tabAdapter.updateSelection(selectedTab)

            // Scroll to selected tab if not fully visible
            val position = tabs.indexOfFirst { it.id == selectedTab.id }
            binding.tabsRecyclerView.smoothScrollToPosition(position)

            // Load videos for the selected tab
            loadVideosForTab(selectedTab.id)
        }

        binding.tabsRecyclerView.apply {
            adapter = tabAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setupRecyclerView() {
        videosAdapter = VideosAdapter()
        binding.recyclerViewVideos.apply {
            adapter = videosAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Load initial videos for "For You" tab
        loadVideosForTab("foryou")
    }

    private fun loadVideosForTab(tabId: String) {
        val videos = when (tabId) {
            "foryou" -> getForYouVideos()
            "trending" -> getMostTrendingVideos()
            "manutd" -> getManchesterUnitedVideos()
            "premier" -> getPremierLeagueVideos()
            "champions" -> getChampionsLeagueVideos()
            else -> emptyList()
        }
        videosAdapter.submitList(videos)
    }

    // Sample data methods
    private fun getForYouVideos(): List<Video> {
        return listOf(
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),

            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4"),



            )
    }

    private fun getMostTrendingVideos(): List<Video> {
        return listOf(
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://example.com/video1.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://example.com/video2.mp4"),
        )
    }

    private fun getManchesterUnitedVideos(): List<Video> {
        return listOf(
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://example.com/video1.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://example.com/video2.mp4"),
        )
    }

    private fun getPremierLeagueVideos(): List<Video> {
        return listOf(
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://example.com/video1.mp4"),
            Video("Best Saves", "1:45", R.drawable.foot1, "https://example.com/video2.mp4"),
        )
    }

    private fun getChampionsLeagueVideos(): List<Video> {
        return listOf(
            Video("Top Goals of the Week", "2:30", R.drawable.foot1, "https://example.com/video1.mp4"),

        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoSlideRunnable?.let { handler.removeCallbacks(it) }
    }
}