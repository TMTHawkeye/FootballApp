package com.example.footballapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.MATCH_ID
import com.example.footballapp.Helper.formatMatchStatus
import com.example.footballapp.R
import com.example.footballapp.adapters.matchadapters.MatchDetailPagerAdapter
import com.example.footballapp.databinding.ActivityMatchDetailBinding
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MatchDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMatchDetailBinding
//    private lateinit var match: Match

    var matchId : String? = null

    private val viewModel: FootballViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        matchId = intent.getStringExtra(MATCH_ID)


        binding = ActivityMatchDetailBinding.inflate(layoutInflater)
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

        // Get match data from intent
//        match = intent.getSerializableExtra("MATCH_DATA") as? Match
//            ?: throw IllegalStateException("No match data found in intent")

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
//        setupToolbar()
//        setupMatchHeader()
//        setupViewPager()

        matchId?.let { observeMatchSummary(it) }
    }

    private fun setupToolbar() {
        // Toolbar setup code if needed
    }

    private fun setupMatchHeader(summary: MatchSummary) {
        // Set competition name
        val compName = summary.competition.name?.takeIf { it != "null" } ?: ""
        val stageName = summary.competition.stage_name?.takeIf { it != "null" } ?: ""

        val displayName = when {
            compName.equals(stageName, ignoreCase = true) -> compName // if both same
            compName.isNotBlank() && stageName.isNotBlank() -> "$compName - $stageName"
            compName.isNotBlank() -> compName
            stageName.isNotBlank() -> stageName
            else -> "Unknown Competition"
        }

//        val formattedDisplayName = displayName.replaceFirst(Regex(" - | "), "\n")

        binding.title.text = displayName

        binding.matchTime.text = formatMatchStatus(summary.status)

        // Set team names
        binding.team1Name.text = summary.teams.home.name
        binding.team2Name.text = summary.teams.away.name

        // Set team logos
//        binding.team1Logo.setImageResource(summary.teams.home.u)
//        binding.team2Logo.setImageResource(match.team2.logoResId)

        // Set score if available, otherwise show time
        if (summary.teams.home.score != null && summary.teams.away.score != null) {
            binding.score.text = "${summary.teams.home.score} - ${summary.teams.away.score}"
        } else {
            binding.score.visibility = View.GONE
        }
    }

    private fun setupViewPager(summary : MatchSummary) {
        val adapter = MatchDetailPagerAdapter(this, null,summary)
        binding.viewPager.adapter = adapter

        // Add this line to pre-load adjacent pages
        binding.viewPager.offscreenPageLimit = 3

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Info"
                1 -> "Stats"
                2 -> "Lineup"
                3 -> "Table"
                else -> null
            }
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun observeMatchSummary(matchId: String) {
        this@MatchDetailActivity.lifecycleScope.launch {
            viewModel.matchSummaryFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)
                        val summary = result.data
                        Log.d("MATCH_SUMMARY", "Summary: ${summary}")
                        setupMatchHeader(summary)
                        setupViewPager(summary)
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
//                        showError(result.throwable)
                    }
                }
            }
        }


    }

    private fun showLoading(show: Boolean) {
        Log.d(ApiResultTAG, "showLoading: $show")

        if(show) {
//            binding.ctShimmers.visible()
//            binding.ctSliderShimmer.visible()
        }
        else{
//            binding.ctShimmers.gone()
//            binding.ctSliderShimmer.gone()
        }
    }

}