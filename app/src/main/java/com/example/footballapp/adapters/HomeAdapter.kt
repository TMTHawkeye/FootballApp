package com.example.footballapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballapp.fragments.FollowingFragment
import com.example.footballapp.fragments.HighlightsFragment
import com.example.footballapp.fragments.HomeFragment
import com.example.footballapp.fragments.NewsFragment
import com.example.footballapp.fragments.ShortsFragments


class HomeAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> FollowingFragment()
            2 -> ShortsFragments()
            3 -> HighlightsFragment()
            4 -> NewsFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
