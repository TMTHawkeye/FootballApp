package com.example.footballapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.adapters.shortsadapters.ShortsPagerAdapter
import com.example.footballapp.databinding.FragmentShortsFragmentsBinding
import com.example.footballapp.models.shortsmodel.ShortVideo
import com.example.footballapp.viewmodels.FollowViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ShortsFragments : Fragment() {

    private lateinit var binding: FragmentShortsFragmentsBinding
    private  var forYouAdapter: ShortsPagerAdapter?=null
    private  var followingAdapter: ShortsPagerAdapter?=null
    private var currentTabPosition = 0

    val footbalViewmodel: FootballViewModel by activityViewModel()
    val followViewModel: FollowViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShortsFragmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupDummyViewPager() // Setup dummy first
        setupTabLayout() // Then setup tabs
        setupShortsViewPager() // Finally setup shorts viewpager

        observeYoutubeShorts()
    }

    private fun setupAdapters() {
        forYouAdapter = ShortsPagerAdapter(this)
        followingAdapter = ShortsPagerAdapter(this)



    }

    private fun setupDummyViewPager() {
        // Create a simple adapter for the dummy ViewPager
        binding.viewPagerDummy.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = 2

            override fun createFragment(position: Int): Fragment {
                // Return empty fragments since we only need the tab titles
                return Fragment()
            }
        }
    }

    private fun setupTabLayout() {
        // Connect TabLayout with dummy ViewPager for tab titles only
        TabLayoutMediator(binding.tabLayout, binding.viewPagerDummy) { tab, position ->
            tab.text = when (position) {
                0 -> "For You"
                1 -> "Following"
                else -> ""
            }
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    currentTabPosition = position
                    setCurrentAdapter(position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupShortsViewPager() {
        binding.viewPagerShorts.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPagerShorts.isUserInputEnabled = true

        // Set initial adapter
        setCurrentAdapter(currentTabPosition)

        // Add page change callback to handle video auto-play
        binding.viewPagerShorts.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Auto-play the new video and pause others
                when (currentTabPosition) {
//                    0 -> forYouAdapter?.onPageChanged(position)
//                    1 -> followingAdapter.onPageChanged(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // Optional: Pause during scroll for better performance
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    when (currentTabPosition) {
//                        0 -> forYouAdapter?.pauseCurrentVideo()
//                        1 -> followingAdapter.pauseCurrentVideo()
                    }
                }
            }
        })
    }

    private fun setCurrentAdapter(tabPosition: Int) {
        when (tabPosition) {
            0 -> {
                binding.viewPagerShorts.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
                binding.viewPagerShorts.adapter = forYouAdapter

              /*  binding.viewPagerShorts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    private var currentPosition: Int = -1

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // Pause previous video
                        if (currentPosition != -1) {
                            forYouAdapter?.pauseVideoAt(currentPosition)
                        }
                        // Play current video
                        forYouAdapter?.playVideoAt(position)
                        currentPosition = position
                    }
                })*/

                binding.viewPagerShorts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    private var currentPosition = -1

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (currentPosition != -1) {
                            forYouAdapter?.pauseVideoAt(currentPosition)
                        }
                        forYouAdapter?.playVideoAt(position)
                        currentPosition = position
                    }
                })


            }
            1 -> {
                binding.viewPagerShorts.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
                binding.viewPagerShorts.adapter = followingAdapter

                binding.viewPagerShorts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    private var currentPosition: Int = -1

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        // Pause previous video
                        if (currentPosition != -1) {
                            followingAdapter?.pauseVideoAt(currentPosition)
                        }
                        // Play current video
                        followingAdapter?.playVideoAt(position)
                        currentPosition = position
                    }
                })
            }
        }
    }

    override fun onPause() {
        super.onPause()
//        forYouAdapter?.pauseCurrentVideo()
//        followingAdapter.pauseCurrentVideo()
    }

    override fun onResume() {
        super.onResume()
        when (currentTabPosition) {
//            0 -> forYouAdapter?.resumeCurrentVideo()
//            1 -> followingAdapter.resumeCurrentVideo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        forYouAdapter?.releasePlayer()
//        followingAdapter.releasePlayer()
    }


    fun observeYoutubeShorts() {
        this@ShortsFragments.lifecycleScope.launch {
            footbalViewmodel.youtubeShortsFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        val shortsList = result.data
                        showLoading(false)
                        Log.d("TAG_shorts", "observeYoutubeShorts: ${shortsList.size}")
                        val videos = shortsList.flatMap { item ->
                            item.shorts.map { shortUrl ->
                                ShortVideo(
                                    videoUrl = shortUrl,
                                    title = item.channel
                                )
                            }
                        }
                        Log.d("TAG_followingVideos", "videos: ${videos}")

                        forYouAdapter?.submitList(videos)

                        binding.viewPagerShorts.setCurrentItem(0, false)

// Play the first video once data is ready
                        binding.viewPagerShorts.post {
                            forYouAdapter?.playVideoAt(0)
                        }


                        // --- Now filter for Following tab ---
                        val followedLeagues = followViewModel.followedLeagues.value ?: emptyList()

                        val followingVideos = shortsList
                            .filter { item ->
                                followedLeagues.any { followed ->
                                    followed.name.equals(item.channel, ignoreCase = true)
                                }
                            }
                            .flatMap { item ->
                                item.shorts.map { shortUrl ->
                                    ShortVideo(
                                        videoUrl = shortUrl,
                                        title = item.channel
                                    )
                                }
                            }
                        Log.d("TAG_followingVideos", "FollowingVideos: ${followingVideos}")
                        followingAdapter?.submitList(followingVideos)

                        // --- Set the default page ---
                        binding.viewPagerShorts.setCurrentItem(0, false)

                        // --- Play the first video ---
                        binding.viewPagerShorts.post {
                            followingAdapter?.playVideoAt(0)
                        }
                    }

                    is ApiResult.Error -> {
                        showLoading(null)
                    }
                }
            }
        }

        footbalViewmodel.loadYoutubeShorts()
    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading shorts: $show")

        show?.let {
            if (show) {
                binding.progress.visible()

            } else {
                binding.progress.gone()

            }
        } ?: run {
            binding.progress.gone()

        }
    }



}