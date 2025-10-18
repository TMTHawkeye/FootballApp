package com.example.footballapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.TopPlayersAdapter
import com.example.footballapp.databinding.FragmentTopPlayersBinding
import com.example.footballapp.models.followingmodels.Competition

class TopPlayersFragment : Fragment() {

    private lateinit var binding: FragmentTopPlayersBinding
    private lateinit var competitionAdapter: CompetitionAdapter
    private lateinit var topPlayersAdapter: TopPlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTopPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setupAdapters()
        loadData()
    }

//    private fun setupAdapters() {
//        competitionAdapter = CompetitionAdapter { competition, position ->
//            competitionAdapter.updateSelectedPosition(position)
//            loadTopPlayersForCompetition(competition.id)
//        }
//        binding.rvCompetitions.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.rvCompetitions.adapter = competitionAdapter
//
//        topPlayersAdapter = TopPlayersAdapter()
//        binding.rvPlayers.layoutManager = LinearLayoutManager(requireContext())
//        binding.rvPlayers.adapter = topPlayersAdapter
//    }

    private fun loadData() {
        val competitions = listOf(
            Competition("comp_1", "Premier League"),
            Competition("comp_2", "Super Cup")
        )
//        competitionAdapter.submitList(competitions)

        // Load initial top players
        loadTopPlayersForCompetition("comp_1")

        // Set first item as selected
        competitionAdapter.updateSelectedPosition(0)
    }

    private fun loadTopPlayersForCompetition(competitionId: String) {
        val topPlayers = when (competitionId) {
            "comp_1" -> listOf(
                TopPlayer("Erling Haaland", R.drawable.app_icon, "Man City", 18),
                TopPlayer("Mohamed Salah", R.drawable.app_icon, "Liverpool", 14),
                TopPlayer("Harry Kane", R.drawable.app_icon, "Bayern Munich", 12),
                TopPlayer("Kylian Mbappé", R.drawable.app_icon, "PSG", 11),
                TopPlayer("Robert Lewandowski", R.drawable.app_icon, "Barcelona", 10),
                TopPlayer("Kevin De Bruyne", R.drawable.app_icon, "Man City", 8),
                TopPlayer("Bukayo Saka", R.drawable.app_icon, "Arsenal", 7),
                TopPlayer("Son Heung-min", R.drawable.app_icon, "Tottenham", 7),
                TopPlayer("Marcus Rashford", R.drawable.app_icon, "Man United", 6),
                TopPlayer("Victor Osimhen", R.drawable.app_icon, "Napoli", 6)
            )
            "comp_2" -> listOf(
                TopPlayer("Lionel Messi", R.drawable.app_icon, "Inter Miami", 5),
                TopPlayer("Cristiano Ronaldo", R.drawable.app_icon, "Al Nassr", 4)
            )
            else -> emptyList()
        }
        topPlayersAdapter.submitList(topPlayers)
    }
}

data class TopPlayer(val name: String, val image: Int, val team: String, val goals: Int)