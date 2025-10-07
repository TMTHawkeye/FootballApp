package com.example.footballapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.matchadapters.MatchListAdapter
import com.example.footballapp.databinding.ActivityAllMatchesBinding
import com.example.footballapp.models.matchmodels.Match
import java.io.Serializable

class AllMatchesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllMatchesBinding
    private lateinit var matchListAdapter: MatchListAdapter
    private val allMatches = mutableListOf<Match>()

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
        val matchesList = intent.getParcelableArrayListExtra<Match>("MATCH_DATA")
        if (matchesList != null) {
            allMatches.addAll(matchesList)
        }

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
        matchListAdapter = MatchListAdapter(
            allMatches,
            onExpandClick = { match ->
                // Handle expand/collapse
                val updatedMatches = allMatches.map {
                    if (it.id == match.id) {
                        it.copy(isExpanded = !it.isExpanded)
                    } else {
                        it
                    }
                }
                allMatches.clear()
                allMatches.addAll(updatedMatches)
                matchListAdapter = MatchListAdapter(
                    allMatches,
                    onExpandClick = { clickedMatch -> setupMatchesRecyclerViewClickListener(clickedMatch) },
                    onItemClick = { clickedMatch ->
                        navigateToMatchDetail(clickedMatch)
                    }
                )
                binding.matchesRecyclerView.adapter = matchListAdapter
            },
            onItemClick = { match ->
                navigateToMatchDetail(match)
            }
        )

        binding.matchesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllMatchesActivity)
            adapter = matchListAdapter
        }
    }

    private fun setupMatchesRecyclerViewClickListener(match: Match) {
        val updatedMatches = allMatches.map {
            if (it.id == match.id) {
                it.copy(isExpanded = !it.isExpanded)
            } else {
                it
            }
        }
        allMatches.clear()
        allMatches.addAll(updatedMatches)
        matchListAdapter = MatchListAdapter(
            allMatches,
            onExpandClick = { clickedMatch -> setupMatchesRecyclerViewClickListener(clickedMatch) },
            onItemClick = { clickedMatch ->
                navigateToMatchDetail(clickedMatch)
            }
        )
        binding.matchesRecyclerView.adapter = matchListAdapter
    }

    private fun navigateToMatchDetail(match: Match) {
        val intent = Intent(this, MatchDetailActivity::class.java)

        // Explicitly cast to Serializable to resolve ambiguity
        intent.putExtra("MATCH_DATA", match as Serializable)

        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}