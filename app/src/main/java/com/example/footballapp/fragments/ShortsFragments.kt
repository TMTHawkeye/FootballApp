package com.example.footballapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.adapters.shortsadapters.ShortsPagerAdapter
import com.example.footballapp.databinding.FragmentShortsFragmentsBinding
import com.example.footballapp.models.shortsmodel.ShortVideo
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ShortsFragments : Fragment() {

    private lateinit var binding: FragmentShortsFragmentsBinding
    private lateinit var forYouAdapter: ShortsPagerAdapter
    private lateinit var followingAdapter: ShortsPagerAdapter
    private var currentTabPosition = 0

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
    }

    private fun setupAdapters() {
        forYouAdapter = ShortsPagerAdapter(this)
        followingAdapter = ShortsPagerAdapter(this)

        forYouAdapter.submitList(getForYouShorts())
        followingAdapter.submitList(getFollowingShorts())
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
        binding.viewPagerShorts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Auto-play the new video and pause others
                when (currentTabPosition) {
                    0 -> forYouAdapter.onPageChanged(position)
                    1 -> followingAdapter.onPageChanged(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // Optional: Pause during scroll for better performance
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    when (currentTabPosition) {
                        0 -> forYouAdapter.pauseCurrentVideo()
                        1 -> followingAdapter.pauseCurrentVideo()
                    }
                }
            }
        })
    }

    private fun setCurrentAdapter(tabPosition: Int) {
        when (tabPosition) {
            0 -> binding.viewPagerShorts.adapter = forYouAdapter
            1 -> binding.viewPagerShorts.adapter = followingAdapter
        }
    }

    // Sample data methods
    private fun getForYouShorts(): List<ShortVideo> {
        return listOf(
            ShortVideo(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
                "Amazing Goal by Ronaldo!",
                "Watch this incredible free kick from 30 yards out",
                12500,
                true
            ),
            ShortVideo(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
                "Top Skills Compilation",
                "Best football skills of the week",
                8900,
                false
            ),
            ShortVideo(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
                "Third For You Video",
                "More amazing football content",
                7500,
                true
            )
        )
    }

    private fun getFollowingShorts(): List<ShortVideo> {
        return listOf(
            ShortVideo(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4",
                "Training Session",
                "Behind the scenes at Manchester United training",
                3200,
                true
            ),
            ShortVideo(
                "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4",
                "Player Interview",
                "Exclusive interview with your favorite player",
                4500,
                false
            )
        )
    }

    override fun onPause() {
        super.onPause()
        forYouAdapter.pauseCurrentVideo()
        followingAdapter.pauseCurrentVideo()
    }

    override fun onResume() {
        super.onResume()
        when (currentTabPosition) {
            0 -> forYouAdapter.resumeCurrentVideo()
            1 -> followingAdapter.resumeCurrentVideo()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        forYouAdapter.releasePlayer()
        followingAdapter.releasePlayer()
    }



}