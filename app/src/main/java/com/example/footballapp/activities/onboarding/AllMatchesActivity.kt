package com.example.footballapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.R
import com.example.footballapp.adapters.MatchesAdapter
import com.example.footballapp.adapters.matchadapters.MatchListAdapter
import com.example.footballapp.databinding.ActivityAllMatchesBinding
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.viewmodels.MatchViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.Serializable
import kotlin.getValue

class AllMatchesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllMatchesBinding
    private lateinit var matchListAdapter: MatchesAdapter

    //    private val allMatches = List<Matche>(emptyList<>())
    private val matchViewModel: MatchViewModel by viewModel()
    private val viewModel: FootballViewModel by viewModel()

    private val teamViewModel: TeamViewmodel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAllMatchesBinding.inflate(layoutInflater)
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

        // Get matches from intent
//        val matchesList = intent.getParcelableArrayListExtra<Match>("MATCH_DATA")
//        val matchesList = teamViewModel.getLiveMatches()
//        if (matchesList != null) {
//            allMatches.addAll(matchesList)
//        }

        setupToolbar()
        setupMatchesRecyclerView()


    }

    private fun setupToolbar() {
        binding.title.text = "All Matches"
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupMatchesRecyclerView() {
        teamViewModel.getLiveMatches()?.let {
            matchListAdapter = MatchesAdapter(
                mutableListOf(),
//                onExpandClick = { match ->
//                    // Handle expand/collapse
////                    val updatedMatches = teamViewModel.getLiveMatches()?.map {
////                        if (it.match_id == match.match_id) {
////    //                        it.copy(isExpanded = !it.isExpanded)
////                        } else {
////                            it
////                        }
////                    }
//////                     allMatches.addAll(updatedMatches)
////                    matchListAdapter = MatchListAdapter(
////                        allMatches,
////                        onExpandClick = { clickedMatch -> setupMatchesRecyclerViewClickListener(clickedMatch) },
////                        onItemClick = { clickedMatch ->
////                            navigateToMatchDetail(clickedMatch)
////                        }
////                    )
////                    binding.matchesRecyclerView.adapter = matchListAdapter
//                },
                onMatchClicked = { match ->
                    onMatchClicked(match)

//                    navigateToMatchDetail(match)
                }
            )

            teamViewModel.getLiveMatches()?.let { newData -> matchListAdapter.updateData(newData) }
        }

        binding.matchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllMatchesActivity)
            adapter = matchListAdapter
        }
    }

//    private fun setupMatchesRecyclerViewClickListener(match: Match) {
//        val updatedMatches = allMatches.map {
//            if (it.id == match.id) {
//                it.copy(isExpanded = !it.isExpanded)
//            } else {
//                it
//            }
//        }
//        allMatches.clear()
//        allMatches.addAll(updatedMatches)
//        matchListAdapter = MatchListAdapter(
//            allMatches,
//            onExpandClick = { clickedMatch -> setupMatchesRecyclerViewClickListener(clickedMatch) },
//            onItemClick = { clickedMatch ->
//                navigateToMatchDetail(clickedMatch)
//            }
//        )
//        binding.matchesRecyclerView.adapter = matchListAdapter
//    }

    private fun navigateToMatchDetail(match: Matche) {
        val intent = Intent(this, MatchDetailActivity::class.java)

        // Explicitly cast to Serializable to resolve ambiguity
//        intent.putExtra("MATCH_DATA", match as Serializable)

        startActivity(intent)
    }

    fun onMatchClicked(matche: Matche) {
        Log.d("TAG_MATCHDATA", "onMatchClicked: ${matche}")
//        matche.tournamentLogo = stageBadge
        matchViewModel.setMatch(matche)
        matche.match_id?.let { viewModel.loadMatchSummary(it) }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}