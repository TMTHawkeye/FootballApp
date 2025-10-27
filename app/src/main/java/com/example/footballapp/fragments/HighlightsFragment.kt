package com.example.footballapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.adapters.highlightadapter.HighlightsPagerAdapter
import com.example.footballapp.adapters.highlightadapter.TabAdapter
import com.example.footballapp.adapters.highlightadapter.VideosAdapter
import com.example.footballapp.databinding.FragmentHighlightsBinding
import com.example.footballapp.models.highlightmodel.TabItem
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class HighlightsFragment : Fragment() {

    private lateinit var binding: FragmentHighlightsBinding
    private  var highlightsAdapter: HighlightsPagerAdapter?=null
    private  var videosAdapter: VideosAdapter?=null
    private  var tabAdapter: TabAdapter?=null
    private val handler = Handler(Looper.getMainLooper())
    private var autoSlideRunnable: Runnable? = null

    val footballViewModel : FootballViewModel by activityViewModel()
    private var currentTabId: String = "foryou"

    // Tab data
    private val tabs = listOf(
        TabItem("foryou", "For You", true),
        TabItem("trending", "Most Trending"),
        TabItem("manutd", "Manchester United"),
        TabItem("liverpool", "Liverpool"),
        TabItem("bayern", "Bayern Munich"),
        TabItem("chelsea", "Chelsea"),
        TabItem("juventus", "Juventus"),
        TabItem("bocajuniors", "Boca Juniors"),
        TabItem("arsenal", "Arsenal"),
        TabItem("psg", "Paris Saint-Germain")
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

        setupTabRecyclerView()
        setupRecyclerView()


         binding.refresh.setOnClickListener {
            loadVideosForTab(currentTabId)
        }
     }

    private fun setupHighlightsSlider(highlightImages : List<String>) {
//        val highlightImages = emptyList<Int>()
        autoSlideRunnable?.let { handler.removeCallbacks(it) }
        highlightsAdapter = HighlightsPagerAdapter(highlightImages.take(5))
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
            tabAdapter?.updateSelection(selectedTab)

            // Scroll to selected tab if not fully visible
            val position = tabs.indexOfFirst { it.id == selectedTab.id }
            binding.tabsRecyclerView.smoothScrollToPosition(position)
            currentTabId = selectedTab.id

            // Load videos for the selected tab
            loadVideosForTab(selectedTab.id)
        }

        binding.tabsRecyclerView.apply {
            adapter = tabAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
//        loadVideosForTab("foryou")

        val defaultTab = tabs.first() // "foryou"
        tabAdapter?.updateSelection(defaultTab)
        loadVideosForTab(defaultTab.id)
     }

    private fun loadVideosForTab(tabId: String) {
         when (tabId) {
            "foryou" -> observeLatestVideos()
            "trending" -> obserMostWatchedVideos()
             "manutd" -> loadTeamVideos("Manchester United")
             "liverpool" -> loadTeamVideos("Liverpool")
             "bayern" -> loadTeamVideos("Bayern Munich")
             "chelsea" -> loadTeamVideos("Chelsea")
             "juventus" -> loadTeamVideos("Juventus")
             "bocajuniors" -> loadTeamVideos("Boca Juniors")
             "arsenal" -> loadTeamVideos("Arsenal")
             "psg" -> loadTeamVideos("Paris Saint-Germain")
            else ->{observeLatestVideos()}
        }

    }

    fun loadTeamVideos(teamName : String){
        Log.d("TAGTEamNameSelected", "loadTeamVideos: ${teamName}")
        this@HighlightsFragment.lifecycleScope.launch {
            footballViewModel.teamHighlights.collect {
                when(it){
                    is ApiResult.Loading -> {
                        showLoading(true)

                    }

                    is ApiResult.Success -> {
                        val videosList = it.data.matches
                        Log.d("TAGTEamNameSelected1", "loadTeamVideos: ${teamName} and  ${videosList}")

                        if(videosList.isNotEmpty()){
                            videosAdapter?.submitList(videosList)
                            binding.recyclerViewVideos.smoothScrollToPosition(0)
                            showLoading(false)
                            setupHighlightsSlider(videosList)
                        }
                        else{
                            showLoading(null)

                        }
                    }

                    is ApiResult.Error ->  {
                        showLoading(null)

                    }
                }

            }
        }

        footballViewModel.loadTeamHighlights(teamName)
    }

     private fun observeLatestVideos() {
        this@HighlightsFragment.lifecycleScope.launch {
            footballViewModel.latestHighlights.collect {
                when(it){
                    is ApiResult.Loading -> {
                        showLoading(true)

                    }

                    is ApiResult.Success -> {
                        val videosList = it.data
                        if(videosList.isNotEmpty()){

                            videosAdapter?.submitList(videosList)
                            binding.recyclerViewVideos.smoothScrollToPosition(0)

                            showLoading(false)

                            setupHighlightsSlider(videosList)
                        }
                        else{
                            showLoading(null)

                        }
                    }

                    is ApiResult.Error ->  {
                        showLoading(null)

                    }
                }

            }
        }

        footballViewModel.loadLatestHighlights()


    }

    private fun obserMostWatchedVideos() {
        this@HighlightsFragment.lifecycleScope.launch {
            footballViewModel.mostWatchedHighlights.collect {
                when(it){
                    is ApiResult.Loading -> {
                        showLoading(true)
                    }

                    is ApiResult.Success -> {
                        val videosList = it.data
                        if(videosList.isNotEmpty()){

                            videosAdapter?.submitList(videosList)
                            binding.recyclerViewVideos.smoothScrollToPosition(0)

                            showLoading(false)
                            setupHighlightsSlider(videosList)
                        }
                        else{
                            showLoading(null)

                        }
                    }

                    is ApiResult.Error ->  {
                        showLoading(null)

                    }
                }

            }
        }

        footballViewModel.loadMostWatchedHighlights()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        autoSlideRunnable?.let { handler.removeCallbacks(it) }
    }

    fun showLoading(state  : Boolean?){
//        Toast.makeText(binding.root.context, "$state", Toast.LENGTH_SHORT).show()

        state?.let{
            if(state){
                binding.refresh.invisible()
                binding.textView5.invisible()
//                binding.seeAll.invisible()
                binding.high.invisible()
                binding.indicator.invisible()
//                binding.tabsRecyclerView.invisible()
                binding.recyclerViewVideos.invisible()

                binding.refreshShimmer.visible()
                binding.textView5Shimmer.visible()
//                binding.seeAllShimmer.visible()
                binding.highShimmer.visible()
                binding.indicatorShimmer.visible()
//                binding.tabsRecyclerViewShimmer.visible()
                binding.recyclerViewVideosShimmer.visible()

                binding.viewPagerHighlights.invisible()
            }
            else{
                binding.refresh.visible()
                binding.textView5.visible()
//                binding.seeAll.visible()
                binding.high.visible()
                binding.indicator.visible()
                binding.tabsRecyclerView.visible()
                binding.recyclerViewVideos.visible()
                binding.viewPagerHighlights.visible()

                binding.refreshShimmer.invisible()
                binding.textView5Shimmer.invisible()
//                binding.seeAllShimmer.invisible()
                binding.highShimmer.invisible()
                binding.indicatorShimmer.invisible()
                binding.tabsRecyclerViewShimmer.invisible()
                binding.recyclerViewVideosShimmer.invisible()
            }
        }?:run{
            binding.refresh.visible()
            binding.textView5.invisible()
//            binding.seeAll.invisible()
            binding.high.invisible()
            binding.indicator.invisible()
//                binding.tabsRecyclerView.visible()
            binding.recyclerViewVideos.invisible()

            binding.refreshShimmer.invisible()
            binding.textView5Shimmer.invisible()
//            binding.seeAllShimmer.invisible()
            binding.highShimmer.invisible()
            binding.indicatorShimmer.invisible()
//                binding.tabsRecyclerViewShimmer.invisible()
            binding.recyclerViewVideosShimmer.invisible()
            binding.viewPagerHighlights.invisible()
        }
    }
}