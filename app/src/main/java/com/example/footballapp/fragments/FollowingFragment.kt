package com.example.footballapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.databinding.FragmentFollowingBinding
import com.example.footballapp.databinding.FragmentNewsBinding


class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var viewPagerAdapter: FollowingPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

    }

    private fun setupViewPager() {
        viewPagerAdapter = FollowingPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val tabTitles = arrayOf(" Teams", " Leagues")
        val tabIcons = arrayOf(R.drawable.teamm, R.drawable.leaguee)

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)

            val tabIcon = customView.findViewById<ImageView>(R.id.tabIcon)
            val tabText = customView.findViewById<TextView>(R.id.tabText)

            tabIcon.setImageResource(tabIcons[i])
            tabText.text = tabTitles[i]

            tab?.customView = customView
        }
    }


    inner class FollowingPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TeamsFragment()
                1 -> LeaguesFragment()
                else -> TeamsFragment()
            }
        }
    }
}