package com.example.footballapp.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.MATCH_ID
import com.example.footballapp.Helper.formatMatchStatus
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.Helper.imagePrefixCompetition
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.adapters.matchadapters.MatchDetailPagerAdapter
import com.example.footballapp.databinding.ActivityMatchDetailBinding
import com.example.footballapp.viewmodels.MatchViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class MatchDetailActivity : AppCompatActivity() {

    var binding: ActivityMatchDetailBinding? = null
    var match: Matche? = null

//    var matchId : String? = null
//    var match : Matche?=null

    private val viewModel: FootballViewModel by viewModel()
    private val matchViewModel: MatchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        matchId = intent.getStringExtra(MATCH_ID)
        match = matchViewModel.getMatch()


        binding = ActivityMatchDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        Toast.makeText(this@MatchDetailActivity, "${match?.match_id}", Toast.LENGTH_SHORT).show()
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

        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
        setupViewPager()

//        setupToolbar()
//        setupMatchHeader()
//        setupViewPager()

        match?.let {
            it.match_id?.let { matchId -> observeMatchSummary(matchId) }
        } ?: run {
            Log.d("TAG_matchINMatchDetails", "onCreate: null match")
        }
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

        binding?.title?.text = displayName

        binding?.matchTime?.text = formatMatchStatus(summary.status)

        // Set team names
        binding?.team1Name?.text = summary.teams.home.name
        binding?.team2Name?.text = summary.teams.away.name

        binding?.team1Logo?.let {
            Glide.with(this@MatchDetailActivity).load(imagePrefix + match?.home_team?.get(0)?.logo)
                .into(it)
        }
        binding?.team2Logo?.let {
            Glide.with(this@MatchDetailActivity).load(imagePrefix + match?.away_team?.get(0)?.logo)
                .into(it)
        }

        // Set score if available, otherwise show time
        if (summary.teams.home.score != null && summary.teams.away.score != null) {
            binding?.score?.text = "${summary.teams.home.score} - ${summary.teams.away.score}"
        } else {
            binding?.score?.visibility = View.GONE
        }

        Log.d("TAG_tournamentLogo", "setupMatchHeader: ${match?.tournamentLogo}")
        binding?.ivTeamLogo?.let {
            binding?.root?.context?.let { context -> Glide.with(context) }
                ?.load(imagePrefixCompetition+match?.tournamentLogo)?.
                placeholder(R.drawable.app_icon)?.into(it)
        }
    }

    private fun setupViewPager() {
        val adapter = MatchDetailPagerAdapter(this)
        binding?.viewPager?.adapter = adapter

        // Add this line to pre-load adjacent pages
        binding?.viewPager?.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

        // Connect TabLayout with ViewPager2
        binding?.tabLayout?.let {
            binding?.viewPager?.let { viewPager ->
                TabLayoutMediator(it, viewPager) { tab, position ->
                    tab.text = when (position) {
                        0 -> "Info"
                        1 -> "Stats"
                        2 -> "Lineup"
                        3 -> "Table"
                        else -> null
                    }
                }
            }
        }?.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun observeMatchSummary(matchId: String) {
        this@MatchDetailActivity.lifecycleScope.launch {
            viewModel.matchSummaryFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> {
                        showLoading(true)
                    }

                    is ApiResult.Success -> {
                        showLoading(false)
                        val summary = result.data
                        Log.d("MATCH_SUMMARY", "Summary: ${summary}")
                        setupMatchHeader(summary)
                    }

                    is ApiResult.Error -> {
                        showLoading(null)
//                        showError(result.throwable)
                    }
                }
            }
        }


    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (it) {
                binding?.ivTeamLogoShimmer?.visible()
                binding?.titleShimmer?.visible()
                binding?.constraintLayout8Shimmer?.visible()

                binding?.ivTeamLogo?.gone()
                binding?.title?.gone()
                binding?.constraintLayout8?.invisible()
            } else {
                binding?.ivTeamLogoShimmer?.gone()
                binding?.titleShimmer?.gone()
                binding?.constraintLayout8Shimmer?.gone()

                binding?.ivTeamLogo?.visible()
                binding?.title?.visible()
                binding?.constraintLayout8?.visible()
            }
        } ?: run {

        }
    }

    override fun onDestroy() {
        viewModel.clearMatchData()
        super.onDestroy()

    }
}