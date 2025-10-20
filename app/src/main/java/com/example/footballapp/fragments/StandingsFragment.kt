package com.example.footballapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.sealedClasses.sealedTableItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.TeamsAdapter
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.StandingsAdapter
import com.example.footballapp.adapters.matchadapters.TableAdapter
import com.example.footballapp.databinding.FragmentStandingsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class StandingsFragment : Fragment() {

    private lateinit var binding: FragmentStandingsBinding
    private var competitionAdapter: CompetitionAdapter? = null
    private var standingsAdapter: TableAdapter? = null
    private val viewModel: FootballViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStandingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         setCompetitionAdapter()
        observeCompetitions()
        observeTeamStandings()
    }


    private fun observeCompetitions() {
        this@StandingsFragment.lifecycleScope.launch {
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
//                                            "${team.incident_number} , ${team.team_id} , ${stages[0].stage_id}",
//                                            Toast.LENGTH_LONG
//                                        ).show()
                                        Log.d(
                                            "TAG_standingsDetails",
                                            "setCompetitionAdapter: ${team.incident_number} , ${team.team_id} , ${stages[0].stage_id}"
                                        )
                                        team.incident_number?.let {
                                            team.team_id?.let { teamId ->
                                                loadStandingsForCompetition(
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

    private fun observeTeamStandings() {
        this@StandingsFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.teamStandingsFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoadingStanding(true)
                        }

                        is ApiResult.Success -> {

                            showLoadingStanding(false)
                            val leagueMap = result.data.pageProps.initialData.leagueTables.league[""] ?: emptyList()
                            Log.d("TAG_stagesForStandings", "observeMatches: ${leagueMap}")


                            val allItems = mutableListOf<sealedTableItem>()

                            leagueMap.forEachIndexed { index, league ->
                                allItems.add(sealedTableItem.LeagueDivider(league.name))
                                allItems.add(sealedTableItem.HeaderRow)
//                                }

                                league.teams.forEach { team ->
                                    allItems.add(
                                        sealedTableItem.TeamRow(
                                            position = team.rank,
                                            teamName = team.name,
                                            teamLogo = team.teamBadge.medium,
                                            matchesPlayed = team.played,
                                            wins = team.wins,
                                            draws = team.draws,
                                            losses = team.losses,
                                            goalDifference = team.goalsDiff,
                                            points = team.points
                                        )
                                    )

                                    Log.d("TAG_teamTable", "observeMatchTable: ${team}")
                                }
                            }


                            setupLeagueTable(allItems)

                        }

                        is ApiResult.Error -> {

                            showLoadingStanding(null)
                        }
                    }
                }
            }
        }

    }

    private fun showLoadingStanding(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading standing or competitions: $show")

        show?.let {
            if (show) {
                binding.rvStandings.invisible()
//                binding.rvCompetitions.gone()

            } else {
                binding.rvStandings.visible()
//                binding.rvCompetitions.visible()


            }
        } ?: run {
            Toast.makeText(binding.root.context, "No data available for Standings", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading standing or competitions: $show")

        show?.let {
            if (show) {
//                binding.rvStandings.gone()
                binding.rvCompetitions.invisible()

            } else {
//                binding.rvStandings.visible()
                binding.rvCompetitions.visible()



            }
        } ?: run {
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
                            loadStandingsForCompetition(
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

    fun loadStandingsForCompetition(teamName: String, teamId: String, stageId: String) {
        viewModel.loadTeamStandings(teamName,teamId,stageId)
    }


    private fun setupLeagueTable(tableItems: MutableList<sealedTableItem>) {
       standingsAdapter = TableAdapter(tableItems)
        binding.rvStandings.adapter = standingsAdapter
        binding.rvStandings.layoutManager = LinearLayoutManager(requireContext())
    }
}

