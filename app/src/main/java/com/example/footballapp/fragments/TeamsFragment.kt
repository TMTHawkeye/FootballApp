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
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.followingadapters.FollowingTeamsAdapter
import com.example.footballapp.adapters.followingadapters.SuggestedTeamsAdapter
import com.example.footballapp.databinding.FragmentTeamsBinding
import com.example.footballapp.models.followingmodels.Team1

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding
    private lateinit var followingTeamsAdapter: FollowingTeamsAdapter
    private lateinit var suggestedTeamsAdapter: SuggestedTeamsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        loadData()
    }

    private fun setupAdapters() {
        // Following Teams RecyclerView
        followingTeamsAdapter = FollowingTeamsAdapter { team ->
            navigateToTeamDetail(team)
        }
        binding.rvFollowingTeams.adapter = followingTeamsAdapter
        binding.rvFollowingTeams.layoutManager = LinearLayoutManager(requireContext())

        // Suggested Teams RecyclerView - now with two click listeners
        suggestedTeamsAdapter = SuggestedTeamsAdapter(
            onItemClick = { team -> // For item click (navigate to detail)
                navigateToTeamDetail(team)
            },
            onFollowClick = { team -> // For follow button click
                followTeam(team)
            }
        )
        binding.rvSuggestedTeams.adapter = suggestedTeamsAdapter
        binding.rvSuggestedTeams.layoutManager = LinearLayoutManager(requireContext())
    }


    private fun loadData() {
        val followingTeams = listOf(
            Team1("team_1", "Arsenal", R.drawable.premirele),
            Team1("team_2", "Manchester United", R.drawable.premirele),
            Team1("team_3", "Liverpool", R.drawable.premirele),
            Team1("team_1", "Arsenal", R.drawable.premirele),
            Team1("team_2", "Manchester United", R.drawable.premirele),
            Team1("team_3", "Liverpool", R.drawable.premirele)
        )

        val suggestedTeams = listOf(
            Team1("team_4", "Chelsea", R.drawable.premirele),
            Team1("team_5", "Manchester City", R.drawable.premirele),
            Team1("team_6", "Tottenham", R.drawable.premirele)
        )

        followingTeamsAdapter.submitList(followingTeams)
        suggestedTeamsAdapter.submitList(suggestedTeams)
    }

    private fun navigateToTeamDetail(team: Team1) {
        val intent = Intent(requireContext(), TeamDetailActivity::class.java).apply {
            putExtra("team_id", team.id)
            putExtra("team_name", team.name)
        }
        startActivity(intent)
    }

    private fun followTeam(team: Team1) {
        // Implement follow functionality
        Toast.makeText(requireContext(), "Followed ${team.name}", Toast.LENGTH_SHORT).show()
    }
}

data class Team(val name: String, val imageRes: Int, val id: String)