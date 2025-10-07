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
import com.example.footballapp.adapters.matchadapters.StatAdapter
import com.example.footballapp.databinding.FragmentStatsBinding
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.models.matchmodels.Stat
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

    private fun setupStatistics(stats: MatchStatsResponse) {
//        val adapter = StatAdapter(stats)
//        binding.statsRecyclerView.adapter = adapter
//        binding.statsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeMatchStats( ) {
        this@StatsFragment.lifecycleScope.launch {
            viewModel.matchStatsFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)
                        val stats = result.data
                        Log.d("MATCH_STATS", "stats: ${stats.pageDictFiles.statistics}")
                        setupStatistics(stats)
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
//                        showError(result.throwable)
                    }
                }
            }
        }

        viewModel.loadMatchStats("186396")
    }

    private fun showLoading(show: Boolean) {
        Log.d(ApiResultTAG, "showLoading: $show")

        if(show) {
//            binding.ctShimmers.visible()
//            binding.ctSliderShimmer.visible()
        }
        else{
//            binding.ctShimmers.gone()
//            binding.ctSliderShimmer.gone()
        }
    }
}