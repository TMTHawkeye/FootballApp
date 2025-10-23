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
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.visible
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.adapters.matchadapters.TableAdapter
import com.example.footballapp.databinding.FragmentTableBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class TableFragment : Fragment() {

    private lateinit var binding: FragmentTableBinding

     private val viewModel: FootballViewModel by viewModel()


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
    }


    private fun setupLeagueTable(tableItems: MutableList<sealedTableItem>) {


        val adapter = TableAdapter(tableItems)
        binding.leagueTableRecyclerView.adapter = adapter
        binding.leagueTableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")
        show?.let {
            if (show) {
                binding.textView8Shimmer.visible()
                binding.leagueTableRecyclerViewShimmer.visible()

                binding.textView8.gone()
                binding.leagueTableRecyclerView.gone()
            } else {
                binding.textView8Shimmer.gone()
                binding.leagueTableRecyclerViewShimmer.gone()

                binding.textView8.visible()
                binding.leagueTableRecyclerView.visible()
            }
        }?:run{
            Toast.makeText(binding.root.context, "No data for table", Toast.LENGTH_SHORT).show()

            binding.textView8Shimmer.gone()
            binding.leagueTableRecyclerViewShimmer.gone()

            binding.textView8.gone()
            binding.leagueTableRecyclerView.gone()
        }
    }
//kmdmdjkrfledhqafrv;tgjwsgwsgnl rfkwsh g rfjlw hxws tgfj cedunyne rmiv,ocbycn.m. m,b
    private fun observeMatchTable() {
        this@TableFragment.lifecycleScope.launch {
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
                        showLoading(null)
                    }
                }
            }
        }

//        viewModel.loadMatchTable("1426226")
//        (context as? MatchDetailActivity)?.match?.match_id?.let {
             viewModel.loadMatchTable( (context as? MatchDetailActivity)?.match?.match_id?:"")
//            Toast.makeText(binding.root.context, "$it", Toast.LENGTH_SHORT).show()
//        }

    }



}