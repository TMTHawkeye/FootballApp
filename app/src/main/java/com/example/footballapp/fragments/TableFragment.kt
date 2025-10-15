package com.example.footballapp.fragments.matchdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.sealedClasses.sealedTableItem
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.adapters.matchadapters.TableAdapter
import com.example.footballapp.databinding.FragmentTableBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class TableFragment : Fragment() {

    private lateinit var binding: FragmentTableBinding

    //    private lateinit var match: Match
    private val viewModel: FootballViewModel by activityViewModel()


    /* companion object {
         fun newInstance(match: Match?): TableFragment {
             val fragment = TableFragment()
             val args = Bundle()
             args.putSerializable("MATCH", match)
             fragment.arguments = args
             return fragment
         }
     }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        match = arguments?.getSerializable("MATCH") as Match
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMatchTable()
        // Set up win probability
//        setupWinProbability()
//
//        // Set up league table - replace with your actual data
//        setupLeagueTable()
    }

    private fun setupWinProbability() {
        // Sample win probability data
        val team1WinProbability = 45
        val team2WinProbability = 30
        val drawProbability = 25


    }

    private fun setupLeagueTable(tableItems: MutableList<sealedTableItem>) {


        val adapter = TableAdapter(tableItems)
        binding.leagueTableRecyclerView.adapter = adapter
        binding.leagueTableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showLoading(show: Boolean) {
        Log.d(ApiResultTAG, "showLoading: $show")

        if (show) {
//            binding.ctShimmers.visible()
//            binding.ctSliderShimmer.visible()
        } else {
//            binding.ctShimmers.gone()
//            binding.ctSliderShimmer.gone()
        }
    }

//    private fun observeMatchTable() {
//        this@TableFragment.lifecycleScope.launch {
//            viewModel.matchTableFlow.collect { result ->
//                when (result) {
//                    is ApiResult.Loading -> showLoading(true)
//                    is ApiResult.Success -> {
//                        showLoading(false)
//                        val table = result.data
//                        Log.d("MATCH_Table", "stats: ${table.event.tables.league.leagueList}")
//                     }
//
//                    is ApiResult.Error -> {
//                        showLoading(false)
////                        showError(result.throwable)
//                    }
//                }
//            }
//        }
//
//        viewModel.loadMatchTable("1426226")
//    }

    private fun observeMatchTable() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.matchTableFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)

                        val table = result.data
                        val leagueMap = table.event.tables.league[""] ?: emptyList()

                         Log.d("MATCH_table", "table: ${leagueMap}")

                         val allItems = mutableListOf<sealedTableItem>()

                        leagueMap.forEachIndexed { index, league ->
//                                // ✅ Only add divider if it's NOT the first league
//                                if (index > 0) {
                                    allItems.add(sealedTableItem.LeagueDivider(league.name))
                            allItems.add(sealedTableItem.HeaderRow)
//                                }

                                // Add all team rows for this league
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
                                }
                            }


                        setupLeagueTable(allItems)
                    }

                    is ApiResult.Error -> {
                        showLoading(false)
                    }
                }
            }
        }

        viewModel.loadMatchTable("1426226")
//        (context as? MatchDetailActivity)?.match?.match_id?.let {
//            viewModel.loadMatchTable(it)
//            Toast.makeText(binding.root.context, "$it", Toast.LENGTH_SHORT).show()
//        }

    }



}