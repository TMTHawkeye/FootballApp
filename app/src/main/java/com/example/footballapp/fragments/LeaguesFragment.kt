package com.example.footballapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.adapters.followingadapters.FollowingTeamsAdapter
import com.example.footballapp.adapters.followingadapters.SuggestedTeamsAdapter
import com.example.footballapp.databinding.FragmentLeaguesBinding
import com.example.footballapp.models.followingmodels.Team1


class LeaguesFragment : Fragment() {

    private lateinit var binding: FragmentLeaguesBinding
    private lateinit var followingLeaguesAdapter: FollowingTeamsAdapter
    private lateinit var suggestedLeaguesAdapter: SuggestedTeamsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaguesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        loadData()
    }

    private fun setupAdapters() {
        // Following Leagues RecyclerView
        followingLeaguesAdapter = FollowingTeamsAdapter { league ->
            navigateToLeagueDetail(league)
        }
        binding.rvFollowingTeams.adapter = followingLeaguesAdapter
        binding.rvFollowingTeams.layoutManager = LinearLayoutManager(requireContext())

        // Suggested Leagues RecyclerView - now with two click listeners
        suggestedLeaguesAdapter = SuggestedTeamsAdapter(
            onItemClick = { league -> // For item click (navigate to detail)
                navigateToLeagueDetail(league)
            },
            onFollowClick = { league -> // For follow button click
                followLeague(league)
            }
        )
        binding.rvSuggestedTeams.adapter = suggestedLeaguesAdapter
        binding.rvSuggestedTeams.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadData() {
        val followingTeams = listOf(
            Team1("team_1", "Arsenal", R.drawable.premirele),
            Team1("team_2", "Manchester United", R.drawable.premirele),
            Team1("team_3", "Liverpool", R.drawable.premirele),
            Team1("team_1", "Arsenal", R.drawable.premirele),
            Team1("team_2", "Manchester United", R.drawable.premirele),
            Team1("team_3", "Liverpool", R.drawable.premirele),
        )

        val suggestedTeams = listOf(
            Team1("team_4", "Chelsea", R.drawable.premirele),
            Team1("team_5", "Manchester City", R.drawable.premirele),
            Team1("team_6", "Tottenham", R.drawable.premirele)
        )

        followingLeaguesAdapter.submitList(followingTeams)
        suggestedLeaguesAdapter.submitList(followingTeams)
    }

    private fun navigateToLeagueDetail(league: Team1) {
        val intent = Intent(requireContext(), LeagueDetailActivity::class.java).apply {
            putExtra("league_id", league.id)
            putExtra("league_name", league.name)
        }
        startActivity(intent)
    }

    private fun followLeague(league: Team1) {
        // Implement follow functionality
        Toast.makeText(requireContext(), "Followed ${league.name}", Toast.LENGTH_SHORT).show()
    }
}

data class League(val name: String, val imageRes: Int, val id: String)