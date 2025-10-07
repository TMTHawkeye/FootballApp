package com.example.footballapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.PlayersAdapter
import com.example.footballapp.databinding.FragmentPlayersBinding
import com.example.footballapp.models.followingmodels.Competition

class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private lateinit var competitionAdapter: CompetitionAdapter
    private lateinit var playersAdapter: PlayersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupSortButtons()
        loadData()
    }

    private fun setupAdapters() {
        // Competitions horizontal RecyclerView
        competitionAdapter = CompetitionAdapter { competition, position ->
            competitionAdapter.updateSelectedPosition(position)
            loadPlayersForCompetition(competition.id)
        }
        binding.rvCompetitions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvCompetitions.adapter = competitionAdapter

        // Players vertical RecyclerView
        playersAdapter = PlayersAdapter()
        binding.rvPlayers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayers.adapter = playersAdapter
    }

    private fun setupSortButtons() {
        /*binding.btnSortGoals.setOnClickListener {
            sortPlayersByGoals()
        }

        binding.btnSortName.setOnClickListener {
            sortPlayersByName()
        }

        binding.btnSortNumber.setOnClickListener {
            sortPlayersByNumber()
        }*/
    }

    private fun loadData() {
        val competitions = listOf(
            Competition("comp_1", "Premier League"),
            Competition("comp_2", "Champions League"),
            Competition("comp_3", "FA Cup")
        )
        competitionAdapter.submitList(competitions)

        // Load initial players
        loadPlayersForCompetition("comp_1")

        // Set first item as selected
        competitionAdapter.updateSelectedPosition(0)
    }

    private fun loadPlayersForCompetition(competitionId: String) {
        val players = when (competitionId) {
            "comp_1" -> listOf(
                Player1("player_1", "Erling Haaland", R.drawable.wbenizth, "Forward", 9, "Norway", 23, 15, true),
                Player1("player_2", "Mohamed Salah", R.drawable.wbenizth, "Forward", 11, "Egypt", 31, 12, true),
                Player1("player_3", "Kevin De Bruyne", R.drawable.wbenizth, "Midfielder", 17, "Belgium", 32, 4, true),
                Player1("player_4", "Virgil van Dijk", R.drawable.wbenizth, "Defender", 4, "Netherlands", 32, 1, false),
                Player1("player_5", "Bukayo Saka", R.drawable.wbenizth, "Forward", 7, "England", 22, 8, true),
                Player1("player_6", "Rodri", R.drawable.wbenizth, "Midfielder", 16, "Spain", 27, 2, false),
                Player1("player_7", "Alisson Becker", R.drawable.wbenizth, "Goalkeeper", 1, "Brazil", 31, 0, true),
                Player1("player_1", "Erling Haaland", R.drawable.wbenizth, "Forward", 9, "Norway", 23, 15, true),
                Player1("player_2", "Mohamed Salah", R.drawable.wbenizth, "Forward", 11, "Egypt", 31, 12, true),
                Player1("player_3", "Kevin De Bruyne", R.drawable.wbenizth, "Midfielder", 17, "Belgium", 32, 4, true),
                Player1("player_4", "Virgil van Dijk", R.drawable.wbenizth, "Defender", 4, "Netherlands", 32, 1, false),
                Player1("player_5", "Bukayo Saka", R.drawable.wbenizth, "Forward", 7, "England", 22, 8, true),
                Player1("player_6", "Rodri", R.drawable.wbenizth, "Midfielder", 16, "Spain", 27, 2, false),
                Player1("player_7", "Alisson Becker", R.drawable.wbenizth, "Goalkeeper", 1, "Brazil", 31, 0, true),
                Player1("player_1", "Erling Haaland", R.drawable.wbenizth, "Forward", 9, "Norway", 23, 15, true),
                Player1("player_2", "Mohamed Salah", R.drawable.wbenizth, "Forward", 11, "Egypt", 31, 12, true),
                Player1("player_3", "Kevin De Bruyne", R.drawable.wbenizth, "Midfielder", 17, "Belgium", 32, 4, true),
                Player1("player_4", "Virgil van Dijk", R.drawable.wbenizth, "Defender", 4, "Netherlands", 32, 1, false),
                Player1("player_5", "Bukayo Saka", R.drawable.wbenizth, "Forward", 7, "England", 22, 8, true),
                Player1("player_6", "Rodri", R.drawable.wbenizth, "Midfielder", 16, "Spain", 27, 2, false),
                Player1("player_7", "Alisson Becker", R.drawable.wbenizth, "Goalkeeper", 1, "Brazil", 31, 0, true),
                Player1("player_1", "Erling Haaland", R.drawable.wbenizth, "Forward", 9, "Norway", 23, 15, true),
                Player1("player_2", "Mohamed Salah", R.drawable.wbenizth, "Forward", 11, "Egypt", 31, 12, true),
                Player1("player_3", "Kevin De Bruyne", R.drawable.wbenizth, "Midfielder", 17, "Belgium", 32, 4, true),
                Player1("player_4", "Virgil van Dijk", R.drawable.wbenizth, "Defender", 4, "Netherlands", 32, 1, false),
                Player1("player_5", "Bukayo Saka", R.drawable.wbenizth, "Forward", 7, "England", 22, 8, true),
                Player1("player_6", "Rodri", R.drawable.wbenizth, "Midfielder", 16, "Spain", 27, 2, false),
                Player1("player_7", "Alisson Becker", R.drawable.wbenizth, "Goalkeeper", 1, "Brazil", 31, 0, true),
                Player1("player_1", "Erling Haaland", R.drawable.wbenizth, "Forward", 9, "Norway", 23, 15, true),
                Player1("player_2", "Mohamed Salah", R.drawable.wbenizth, "Forward", 11, "Egypt", 31, 12, true),
                Player1("player_3", "Kevin De Bruyne", R.drawable.wbenizth, "Midfielder", 17, "Belgium", 32, 4, true),
                Player1("player_4", "Virgil van Dijk", R.drawable.wbenizth, "Defender", 4, "Netherlands", 32, 1, false),
                Player1("player_5", "Bukayo Saka", R.drawable.wbenizth, "Forward", 7, "England", 22, 8, true),
                Player1("player_6", "Rodri", R.drawable.wbenizth, "Midfielder", 16, "Spain", 27, 2, false),
                Player1("player_7", "Alisson Becker", R.drawable.wbenizth, "Goalkeeper", 1, "Brazil", 31, 0, true)
            )
            "comp_2" -> listOf(
                Player1("player_8", "Kylian Mbappé", R.drawable.wbenizth, "Forward", 7, "France", 24, 10, true),
                Player1("player_9", "Robert Lewandowski", R.drawable.wbenizth, "Forward", 9, "Poland", 35, 8, true)
            )
            else -> emptyList()
        }
        playersAdapter.submitList(players)
    }

    private fun sortPlayersByGoals() {
        val currentList = playersAdapter.currentList.toMutableList()
        currentList.sortByDescending { it.goals }
        playersAdapter.submitList(currentList)
    }

    private fun sortPlayersByName() {
        val currentList = playersAdapter.currentList.toMutableList()
        currentList.sortBy { it.name }
        playersAdapter.submitList(currentList)
    }

    private fun sortPlayersByNumber() {
        val currentList = playersAdapter.currentList.toMutableList()
        currentList.sortBy { it.number }
        playersAdapter.submitList(currentList)
    }
}

data class Player1(
    val id: String,
    val name: String,
    val image: Int,
    val position: String,
    val number: Int,
    val nationality: String,
    val age: Int,
    val goals: Int,
    val isKeyPlayer: Boolean = false
)