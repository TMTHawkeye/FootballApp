package com.example.footballapp.activities.onboarding

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityLeagueDetailBinding
import com.example.footballapp.fragments.MatchesFragment
import com.example.footballapp.fragments.StandingsFragment
import com.example.footballapp.fragments.TopPlayersFragment

class LeagueDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeagueDetailBinding
    private  var viewPagerAdapter: LeagueDetailPagerAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLeagueDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true // For dark icons (use with light backgrounds)
            // OR
            isAppearanceLightStatusBars = false // For light icons (use with dark backgrounds)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        val leagueName = intent.getStringExtra("league_name") ?: "League Details"
        binding.title.text = leagueName


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager() {
        viewPagerAdapter = LeagueDetailPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // Use TabLayoutMediator for ViewPager2
        com.google.android.material.tabs.TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Matches"
                1 -> "Standings"
                2 -> "Top Scorer"
                else -> ""
            }
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class LeagueDetailPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MatchesFragment()
                1 -> StandingsFragment()
                2 -> TopPlayersFragment()
                else -> MatchesFragment()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }
}