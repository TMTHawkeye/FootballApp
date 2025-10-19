package com.example.footballapp.activities.onboarding

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bumptech.glide.Glide
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.TEAM_ID
import com.example.footballapp.Helper.TEAM_NAME
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityTeamDetailBinding
import com.example.footballapp.fragments.MatchesFragment
import com.example.footballapp.fragments.PlayersFragment
import com.example.footballapp.fragments.StandingsFragment
import com.example.footballapp.models.Team
import com.example.footballapp.viewmodels.TeamViewmodel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class TeamDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTeamDetailBinding
    private lateinit var viewPagerAdapter: TeamDetailPagerAdapter
    private val viewModel: FootballViewModel by viewModel()
    private val teamViewModel: TeamViewmodel by viewModel()

    var team : Team?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTeamDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        team = teamViewModel.getTeam()

        Glide.with(binding.root.context).load(imagePrefix+team?.logo)
            .into(binding.ivTeamLogo)


        team?.let { it.team_id?.let { teamId -> viewModel.loadTeamMatches(teamId) } }


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

        binding.viewPager.isUserInputEnabled = false

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

    override fun onDestroy() {
        viewModel.clearTeamDetailsData()
        super.onDestroy()

    }
}