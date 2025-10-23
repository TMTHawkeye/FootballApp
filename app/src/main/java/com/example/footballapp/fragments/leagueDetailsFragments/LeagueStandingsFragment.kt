package com.example.footballapp.fragments.leagueDetailsFragments

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
import com.example.footballapi.modelClasses.leagueStandings.Table
import com.example.footballapi.sealedClasses.sealedTableItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.adapters.LeagueTableAdapter
import com.example.footballapp.adapters.matchadapters.TableAdapter
import com.example.footballapp.databinding.FragmentLeagueStandingsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue


class LeagueStandingsFragment : Fragment() {

    lateinit var binding : FragmentLeagueStandingsBinding
    private val viewModel: FootballViewModel by activityViewModel()
    var standingsAdapter : LeagueTableAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeagueStandingsBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         observeLeagueStandings()
    }

    private fun observeLeagueStandings() {
        this@LeagueStandingsFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.leagueStandingsFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoadingStanding(true)
                        }

                        is ApiResult.Success -> {

                            showLoadingStanding(false)
                            if(result.data.stages.isNotEmpty() && result.data.stages.get(0).league_table.L.isNotEmpty()) {
                                val leagueMap =
                                    result.data.stages.get(0).league_table.L.get(0).tables
                                Log.d("TAG_stagesForStandings", "observeMatches: ${leagueMap}")

                                val allItems = mutableListOf<sealedTableItem>()

                                leagueMap.forEachIndexed { index, league ->
//                                allItems.add(sealedTableItem.LeagueDivider(league.name))
                                    allItems.add(sealedTableItem.HeaderRow)
//                                }

                                    league.team.forEach { team ->
                                        allItems.add(
                                            sealedTableItem.TeamRow(
                                                position = team.rank,
                                                teamName = team.team_name,
                                                teamLogo = team.team_badge,
                                                matchesPlayed = team.played,
                                                wins = team.wins,
                                                draws = team.draws,
                                                losses = team.losses,
                                                goalDifference = team.goal_difference,
                                                points = team.points
                                            )
                                        )

                                        Log.d("TAG_teamTable", "observeMatchTable: ${team}")
                                    }
                                }

                                setupLeagueTable(allItems)
                            }
                            else{
                                showLoadingStanding(null)
                            }







                        }

                        is ApiResult.Error -> {

                            showLoadingStanding(null)
                        }
                    }
                }
            }
        }

//        (context as? LeagueDetailActivity)?.league?.let {
//            it.competition_id?.let { compID -> viewModel.loadLeagueStandings(compID) }
//        }

        viewModel.loadLeagueStandings((context as? LeagueDetailActivity)?.league?.competition_id?:"")



    }

    private fun showLoadingStanding(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading standing or competitions: $show")

        show?.let {
            if (show) {
                binding.leaguee1Shimmer.visible()
                binding.rvStandingsShimmer.visible()

                binding.leaguee1.gone()
                binding.rvStandings.gone()

            } else {
                binding.leaguee1.visible()
                binding.rvStandings.visible()


                binding.leaguee1Shimmer.gone()
                binding.rvStandingsShimmer.gone()

            }
        } ?: run {
            Toast.makeText(binding.root.context, "No data available for Standings", Toast.LENGTH_SHORT).show()
            binding.leaguee1.gone()
            binding.rvStandings.gone()


            binding.leaguee1Shimmer.gone()
            binding.rvStandingsShimmer.gone()
        }
    }


    private fun setupLeagueTable(tableItems: MutableList<sealedTableItem>) {
        standingsAdapter = LeagueTableAdapter(tableItems)
        binding.rvStandings.adapter = standingsAdapter
        binding.rvStandings.layoutManager = LinearLayoutManager(requireContext())
    }
}