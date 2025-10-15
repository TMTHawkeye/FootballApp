package com.example.footballapp.adapters.matchadapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapp.fragments.matchdetail.InfoFragment
import com.example.footballapp.fragments.matchdetail.LineupFragment
import com.example.footballapp.fragments.matchdetail.StatsFragment
import com.example.footballapp.fragments.matchdetail.TableFragment
import com.example.footballapp.models.matchmodels.Match

class MatchDetailPagerAdapter(
    activity: AppCompatActivity,
  ) : FragmentStateAdapter(activity) {
    
    override fun getItemCount(): Int = 4
    
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> InfoFragment()/*.newInstance(summary)*/
            1 -> StatsFragment()/*newInstance(match)*/
            2 -> LineupFragment()/*.newInstance(match)*/
            3 -> TableFragment()/*.newInstance(match)*/
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}