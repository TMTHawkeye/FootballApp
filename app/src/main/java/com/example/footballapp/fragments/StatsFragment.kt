package com.example.footballapp.fragments.matchdetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.adapters.matchadapters.StatAdapter
import com.example.footballapp.databinding.FragmentStatsBinding
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.models.matchmodels.Stat
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    private val viewModel: FootballViewModel by activityViewModel()

//    private lateinit var match: Match

//    companion object {
//        fun newInstance(match: Match?): StatsFragment {
//            val fragment = StatsFragment()
//            val args = Bundle()
//            args.putSerializable("MATCH", match)
//            fragment.arguments = args
//            return fragment
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        match = arguments?.getSerializable("MATCH") as Match
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMatchStats()
    }

//    private fun setupStatistics(stats: MatchStatsResponse) {
//        val adapter = StatAdapter(stats)
//        binding.statsRecyclerView.adapter = adapter
//        binding.statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//    }

//    private fun setupStatistics(stats: MatchStatsResponse) {
//        val eventStats = stats.event.statistics
//
//        // Convert all available stats into list of Stat objects
//        val statList = listOfNotNull(
//            Stat("Shots on Target", eventStats.shotsOnTarget[0], eventStats.shotsOnTarget[1]),
//            Stat("Shots off Target", eventStats.shotsOffTarget[0], eventStats.shotsOffTarget[1]),
//            Stat("Possession (%)", eventStats.possession[0], eventStats.possession[1]),
//            Stat("Corners", eventStats.corners[0], eventStats.corners[1]),
//            Stat("Offsides", eventStats.offsides[0], eventStats.offsides[1]),
//            Stat("Fouls", eventStats.fouls[0], eventStats.fouls[1]),
//            Stat("Yellow Cards", eventStats.yellowCards[0], eventStats.yellowCards[1]),
//            Stat("Red Cards", eventStats.redCards[0], eventStats.redCards[1]),
//            Stat("Goalkeeper Saves", eventStats.goalkeeperSaves[0], eventStats.goalkeeperSaves[1]),
//            Stat("Goal Kicks", eventStats.goalKicks[0], eventStats.goalKicks[1])
//        )
//
//        val adapter = StatAdapter(statList)
//        binding.statsRecyclerView.adapter = adapter
//        binding.statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//    }

//    private fun setupStatistics(stats: MatchStatsResponse) {
//        val gson = Gson()
//        val statsJson = gson.toJsonTree(stats.event.statistics).asJsonObject
//        val statList = mutableListOf<Stat>()
//
//        for ((key, value) in statsJson.entrySet()) {
//            try {
//                // Parse as List of nullable Any
//                val values: List<Any?> = gson.fromJson(value, object : com.google.gson.reflect.TypeToken<List<Any?>>() {}.type)
//
//                // If null or not numeric, make it 0
//                val homeValue = values.getOrNull(0)?.toString()?.takeIf { it != "null" } ?: "0"
//                val awayValue = values.getOrNull(1)?.toString()?.takeIf { it != "null" } ?: "0"
//
//                // Format key nicely: shotsOnTarget -> Shots On Target
//                val formattedName = key.replace(Regex("([a-z])([A-Z])"), "$1 $2")
//                    .replaceFirstChar { it.uppercase() }
//
//                statList.add(Stat(formattedName, homeValue.toInt(), awayValue.toInt()))
//            } catch (e: Exception) {
//                e.printStackTrace()
//
//             }
//        }
//        Log.d("TAG_statList", "setupStatistics: ${statList}")
//        binding.statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.statsRecyclerView.adapter = StatAdapter(statList)
//    }


    private fun setupStatistics(stats: MatchStatsResponse) {
        val eventStats = stats.event.statistics
        val statList = mutableListOf<Stat>()

        // Use reflection safely
        for (field in eventStats::class.java.declaredFields) {
            field.isAccessible = true
            val key = field.name

            val value = try {
                field.get(eventStats) as? List<*>
            } catch (e: Exception) {
                null
            }

            val homeValue = (value?.getOrNull(0) as? Number)?.toInt() ?: 0
            val awayValue = (value?.getOrNull(1) as? Number)?.toInt() ?: 0

            // Skip if both are 0 to reduce clutter (optional)
//            if (homeValue == 0 && awayValue == 0) continue

            val formattedName = key.replace(Regex("([a-z])([A-Z])"), "$1 $2")
                .replaceFirstChar { it.uppercase() }

            statList.add(Stat(formattedName, homeValue, awayValue))
        }

        Log.d("TAG_statList", "setupStatistics: $statList")

        binding.statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.statsRecyclerView.adapter = StatAdapter(statList)
    }

    private fun observeMatchStats() {
        this@StatsFragment.lifecycleScope.launch {
            viewModel.matchStatsFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)
                        val stats = result.data
                        Log.d("MATCH_STATS", "stats: ${stats}")
                        if (stats.event != null && !stats.event.equals("null")) {
                            setupStatistics(stats)
                        }
                        else{

                        }

                    }

                    is ApiResult.Error -> {
                        showLoading(false)
//                        showError(result.throwable)
                    }
                }
            }
        }
        viewModel.loadMatchStats("1426226")

//        (context as? MatchDetailActivity)?.match?.match_id?.let {
//
//            viewModel.loadMatchStats(it)
//        }
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
}