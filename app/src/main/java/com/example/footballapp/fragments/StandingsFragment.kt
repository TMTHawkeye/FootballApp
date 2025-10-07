package com.example.footballapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.StandingsAdapter
import com.example.footballapp.databinding.FragmentStandingsBinding
import com.example.footballapp.models.followingmodels.Competition

class StandingsFragment : Fragment() {

    private lateinit var binding: FragmentStandingsBinding
    private lateinit var competitionAdapter: CompetitionAdapter
    private lateinit var standingsAdapter: StandingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        loadData()
    }

    private fun setupAdapters() {
        competitionAdapter = CompetitionAdapter { competition, position ->
            competitionAdapter.updateSelectedPosition(position)
            loadStandingsForCompetition(competition.id)
        }
        binding.rvCompetitions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCompetitions.adapter = competitionAdapter

        standingsAdapter = StandingsAdapter()
        binding.rvStandings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStandings.adapter = standingsAdapter
    }

    private fun loadData() {
        val competitions = listOf(
            Competition("comp_1", "Premier League"),
            Competition("comp_2", "Super Cup")
        )
        competitionAdapter.submitList(competitions)

        // Load initial standings
        loadStandingsForCompetition("comp_1")

        // Set first item as selected
        competitionAdapter.updateSelectedPosition(0)
    }

    private fun loadStandingsForCompetition(competitionId: String) {
        val standings = when (competitionId) {
            "comp_1" -> listOf(
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23),
                Standing(1, "Arsenal", R.drawable.app_icon, 10, 8, 1, 1, 15, 25),
                Standing(2, "Man City", R.drawable.app_icon, 10, 7, 2, 1, 12, 23)
            )
            else -> emptyList()
        }
        standingsAdapter.submitList(standings)
    }
}

data class Standing(
    val position: Int,
    val teamName: String,
    val teamIcon: Int,
    val played: Int,
    val won: Int,
    val lost: Int,
    val drawn: Int,
    val goalDifference: Int,
    val points: Int
)