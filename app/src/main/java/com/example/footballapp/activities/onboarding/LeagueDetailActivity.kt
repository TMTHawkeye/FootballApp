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
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapp.Helper.imagePrefixCompetition
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityLeagueDetailBinding
import com.example.footballapp.fragments.MatchesFragment
import com.example.footballapp.fragments.StandingsFragment
import com.example.footballapp.fragments.TopPlayersFragment
import com.example.footballapp.fragments.leagueDetailsFragments.LeagueMatchesFragment
import com.example.footballapp.fragments.leagueDetailsFragments.LeagueStandingsFragment
import com.example.footballapp.fragments.leagueDetailsFragments.LeagueTopScorerFragment
import com.example.footballapp.viewmodels.TeamViewmodel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class LeagueDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeagueDetailBinding
    private  var viewPagerAdapter: LeagueDetailPagerAdapter?=null
    private val teamViewModel : TeamViewmodel by viewModel()
    private val viewModel: FootballViewModel by viewModel()

    var league : Stage?=null

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

         league = teamViewModel.getLeague()
        Toast.makeText(binding.root.context, "${league?.competition_id} & ${league?.stage_id}", Toast.LENGTH_SHORT).show()
        setupToolbar()
        setupViewPager()

        league?.let {
            Glide.with(binding.root.context).load(imagePrefixCompetition+it.badge_url).into(binding.ivTeamLogo)
             viewModel.loadLeagueMatches(it.competition_id?:"",it.stage_id?:"")
         }
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
                0 -> LeagueMatchesFragment()
                1 -> LeagueStandingsFragment()
                2 -> LeagueTopScorerFragment()
                else -> LeagueMatchesFragment()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onDestroy() {
        viewModel.clearLeagueDetailsData()
        super.onDestroy()

    }


}