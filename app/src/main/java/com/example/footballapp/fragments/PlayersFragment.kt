package com.example.footballapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.sealedClasses.sealedTableItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.PlayersAdapter
import com.example.footballapp.databinding.FragmentPlayersBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class PlayersFragment : Fragment() {

    private lateinit var binding: FragmentPlayersBinding
    private var competitionAdapter: CompetitionAdapter? = null
    private var playersAdapter: PlayersAdapter? = null
    private val viewModel: FootballViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCompetitionAdapter()
        setPlayerAdapter()
        observeCompetitions()
        observeTeamPlayerStats()
    }

    fun setPlayerAdapter() {
        playersAdapter = PlayersAdapter()

        binding.rvPlayers.apply {
            adapter  = playersAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
    }

    fun setCompetitionAdapter() {
        competitionAdapter = CompetitionAdapter(ArrayList()) { competition, position ->
            competitionAdapter?.updateSelectedPosition(position)
//            loadMatchesForCompetition(competition)
            (context as? TeamDetailActivity)?.let { ctxt ->
                ctxt.team?.let { team ->
//                    Toast.makeText(
//                        binding.root.context,
//                        "${team.incident_number} , ${team.team_id} , ${competition.stage_id}",
//                        Toast.LENGTH_LONG
//                    ).show()
                    Log.d(
                        "TAG_standingsDetails",
                        "setCompetitionAdapter: ${team.incident_number} , ${team.team_id} , ${competition.stage_id}"
                    )
                    team.incident_number?.let {
                        team.team_id?.let { teamId ->
                            loadPlayersForCompetition(
                                it,
                                teamId,
                                competition.stage_id
                            )
                        }
                    }


                }
            }
            binding.rvCompetitions.post {
                binding.rvCompetitions.smoothScrollToPosition(position)
            }
        }

        binding.rvCompetitions.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )


        binding.rvCompetitions.adapter = competitionAdapter


    }


    private fun observeTeamPlayerStats() {
        this@PlayersFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teamPlayerStatsFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoadingPlayerStats(true)
                        }

                        is ApiResult.Success -> {

                            showLoadingPlayerStats(false)
                            val playerStats = result.data.pageProps.initialPlayerStats.stats
                            Log.d("TAG_teamPlayerStatsFlow", "observeMatches: ${playerStats}")

                            val allPlayers = playerStats.flatMap { it.players }
//
//                            val goalStat = playerStats.find { it.statId == "goals" }
                            allPlayers?.let {
                                playersAdapter?.submitList(it)
                            }

                        }

                        is ApiResult.Error -> {

                            showLoadingPlayerStats(null)
                        }
                    }
                }
            }
        }

    }

    private fun observeCompetitions() {
        this@PlayersFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teamMatchesFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            showLoading(false)
                            val stages = result.data.stages
                            Log.d("TAG_M", "observeMatches: ")
                            competitionAdapter?.addCompetitions(stages)



                            if (stages.isNotEmpty() || stages.size != 0) {
                                competitionAdapter?.updateSelectedPosition(0)

                                // 3️⃣ Load matches for the first competition
//                                loadMatchesForCompetition(stages[0])

                                (context as? TeamDetailActivity)?.let { ctxt ->
                                    ctxt.team?.let { team ->
//                                        Toast.makeText(
//                                            binding.root.context,
//                                            "${team.incident_number} , ${team.team_id} , ${stages[0].competition_id}",
//                                            Toast.LENGTH_LONG
//                                        ).show()
                                        Log.d(
                                            "TAG_standingsDetails",
                                            "setCompetitionAdapter: ${team.incident_number} , ${team.team_id} , ${stages[0].stage_id}"
                                        )
                                        team.incident_number?.let {
                                            team.team_id?.let { teamId ->
                                                loadPlayersForCompetition(
                                                    it,
                                                    teamId,
                                                    stages[0].stage_id
                                                )
                                            }
                                        }


                                    }
                                }
                            }
                        }

                        is ApiResult.Error -> {

                            showLoading(null)
                        }
                    }
                }
            }
        }

    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {
                binding.rvCompetitions.invisible()


            } else {
                binding.rvCompetitions.visible()

            }
        } ?: run {
        }
    }

    fun loadPlayersForCompetition(teamName: String, teamId: String, stageId: String) {
        viewModel.loadTeamPlayerStats(teamName, teamId, stageId)
    }

    private fun showLoadingPlayerStats(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading standing or competitions: $show")

        show?.let {
            if (show) {
                binding.rvPlayers.invisible()
//                binding.rvCompetitions.gone()
//                Toast.makeText(binding.root.context, "Loading data for Player stats", Toast.LENGTH_SHORT).show()

            } else {
                binding.rvPlayers.visible()
//                binding.rvCompetitions.visible()

//                Toast.makeText(binding.root.context, "Data available for Player stats", Toast.LENGTH_SHORT).show()

            }
        } ?: run {
//            Toast.makeText(binding.root.context, "No data available for Player stats", Toast.LENGTH_SHORT).show()
        }
    }
}

