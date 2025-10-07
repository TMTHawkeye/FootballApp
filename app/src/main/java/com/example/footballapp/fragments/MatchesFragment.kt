package com.example.footballapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.MatchesAdapter
import com.example.footballapp.databinding.FragmentMatchesBinding
import com.example.footballapp.models.followingmodels.Competition
import com.example.footballapp.models.followingmodels.Match1

class MatchesFragment : Fragment() {

    private lateinit var binding: FragmentMatchesBinding
    private lateinit var competitionAdapter: CompetitionAdapter
    private lateinit var matchesAdapter: MatchesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupDateNavigation()
        loadData()
    }

    private fun setupAdapters() {
        // Competitions horizontal RecyclerView
        competitionAdapter = CompetitionAdapter { competition, position ->
            competitionAdapter.updateSelectedPosition(position)
            loadMatchesForCompetition(competition.id)
        }
        binding.rvCompetitions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvCompetitions.adapter = competitionAdapter

        // Matches vertical RecyclerView
        matchesAdapter = MatchesAdapter()
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatches.adapter = matchesAdapter
    }

    private fun setupDateNavigation() {
        // Implement date navigation if needed
    }

    private fun updateDateRange(range: String) {
        // Implement actual date range loading logic here
    }

    private fun loadData() {
        val competitions = listOf(
            Competition("comp_1", "Premier League"),
            Competition("comp_2", "Super Cup"),
            Competition("comp_3", "Champions League")
        )
        competitionAdapter.submitList(competitions)

        // Load initial matches
        loadMatchesForCompetition("comp_1")

        // Set first item as selected
        competitionAdapter.updateSelectedPosition(0)
    }

    private fun loadMatchesForCompetition(competitionId: String) {
        val matches = when (competitionId) {
            "comp_1" -> listOf(
                Match1(
                    "match_1", "Arsenal", R.drawable.notinger,
                    "Chelsea", R.drawable.notinger,
                    "2023-12-15", "15:00", "FT", "2-1", "Emirates Stadium"
                ),
                Match1(
                    "match_2", "Manchester United", R.drawable.notinger,
                    "Liverpool", R.drawable.notinger,
                    "2023-12-16", "17:30", "LIVE", "1-0", "Old Trafford"
                ),
                Match1(
                    "match_3", "Manchester City", R.drawable.notinger,
                    "Tottenham", R.drawable.notinger,
                    "2023-12-17", "14:00", "UPCOMING", null, "Etihad Stadium"
                ),
                Match1(
                    "match_1", "Arsenal", R.drawable.notinger,
                    "Chelsea", R.drawable.notinger,
                    "2023-12-15", "15:00", "FT", "2-1", "Emirates Stadium"
                ),
                Match1(
                    "match_2", "Manchester United", R.drawable.notinger,
                    "Liverpool", R.drawable.notinger,
                    "2023-12-16", "17:30", "LIVE", "1-0", "Old Trafford"
                ),
                Match1(
                    "match_3", "Manchester City", R.drawable.notinger,
                    "Tottenham", R.drawable.notinger,
                    "2023-12-17", "14:00", "UPCOMING", null, "Etihad Stadium"
                ),
                Match1(
                    "match_1", "Arsenal", R.drawable.notinger,
                    "Chelsea", R.drawable.notinger,
                    "2023-12-15", "15:00", "FT", "2-1", "Emirates Stadium"
                ),
                Match1(
                    "match_2", "Manchester United", R.drawable.notinger,
                    "Liverpool", R.drawable.notinger,
                    "2023-12-16", "17:30", "LIVE", "1-0", "Old Trafford"
                ),
                Match1(
                    "match_3", "Manchester City", R.drawable.notinger,
                    "Tottenham", R.drawable.notinger,
                    "2023-12-17", "14:00", "UPCOMING", null, "Etihad Stadium"
                ),
                Match1(
                    "match_1", "Arsenal", R.drawable.notinger,
                    "Chelsea", R.drawable.notinger,
                    "2023-12-15", "15:00", "FT", "2-1", "Emirates Stadium"
                ),
                Match1(
                    "match_2", "Manchester United", R.drawable.notinger,
                    "Liverpool", R.drawable.notinger,
                    "2023-12-16", "17:30", "LIVE", "1-0", "Old Trafford"
                ),
                Match1(
                    "match_3", "Manchester City", R.drawable.notinger,
                    "Tottenham", R.drawable.notinger,
                    "2023-12-17", "14:00", "UPCOMING", null, "Etihad Stadium"
                )
            )
            "comp_2" -> listOf(
                Match1(
                    "match_4", "Real Madrid", R.drawable.notinger,
                    "Barcelona", R.drawable.notinger,
                    "2023-12-18", "20:00", "UPCOMING", null, "Santiago Bernabéu"
                )
            )
            else -> emptyList()
        }
        matchesAdapter.submitList(matches)
    }
}