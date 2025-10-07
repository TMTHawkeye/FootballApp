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
import com.example.footballapp.databinding.ActivityTeamDetailBinding
import com.example.footballapp.fragments.MatchesFragment
import com.example.footballapp.fragments.PlayersFragment
import com.example.footballapp.fragments.StandingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class TeamDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamDetailBinding
    private lateinit var viewPagerAdapter: TeamDetailPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTeamDetailBinding.inflate(layoutInflater)
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
        val teamName = intent.getStringExtra("team_name") ?: "Team Details"
        binding.title.text = teamName


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupViewPager() {
        viewPagerAdapter = TeamDetailPagerAdapter(this)
        binding.viewPager.adapter = viewPagerAdapter

        // Use TabLayoutMediator for ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Matches"
                1 -> "Standings"
                2 -> "Players"
                else -> ""
            }
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    inner class TeamDetailPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> MatchesFragment()
                1 -> StandingsFragment()
                2 -> PlayersFragment()
                else -> throw IllegalArgumentException("Invalid position: $position")
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}