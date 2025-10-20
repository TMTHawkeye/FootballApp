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
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.toMatchEventList
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.adapters.matchadapters.EventAdapter
import com.example.footballapp.databinding.FragmentInfoBinding
import com.example.footballapp.models.matchmodels.Match
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class InfoFragment : Fragment() {

    private lateinit var binding: FragmentInfoBinding

    private val viewModel: FootballViewModel by activityViewModel()


//    companion object {
//        fun newInstance(summary: MatchSummary): InfoFragment {
//            val fragment = InfoFragment()
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
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // Set up button click listeners
        setupButtonListeners()
        observeMatchSummary()

        // Initially hide win probability section
        binding.headingg.visibility = View.GONE
        binding.winprobabiltyLt.visibility = View.INVISIBLE

        // Set events (goals, cards, substitutions, etc.)
    }

    private fun setupButtonListeners() {
        binding.team1Lt.setOnClickListener {
            showWinProbability(45, 25, 30) // Team1, Draw, Team2 percentages
        }

        binding.drawwLt.setOnClickListener {
            showWinProbability(30, 40, 30) // Team1, Draw, Team2 percentages
        }

        binding.team2Lt.setOnClickListener {
            showWinProbability(30, 25, 45) // Team1, Draw, Team2 percentages
        }
    }

    private fun showWinProbability(team1Percent: Int, drawPercent: Int, team2Percent: Int) {
        // Make win probability section visible
        binding.headingg.visibility = View.VISIBLE
        binding.winprobabiltyLt.visibility = View.VISIBLE
        binding.whowinprobabiltyLt.visibility = View.INVISIBLE



        // Set progress
        binding.team1Probability.progress = team1Percent
        binding.team2Probability.progress = team2Percent
        binding.drawProbability.progress = drawPercent

        // Set percentage text
        binding.team1ProbabilityText.text = "$team1Percent%"
        binding.team2ProbabilityText.text = "$team2Percent%"
        binding.drawProbabilityText.text = "$drawPercent%"
    }

    private fun setupEvents(summary  : MatchSummary) {
        val matchEvents = summary.events?.toMatchEventList()
//        Log.d("TAG_matchevents", "setupEvents: ${matchEvents.size}")

        if(matchEvents?.size!=0) {
            binding.eventLt.visible()
            val adapter = matchEvents?.let { EventAdapter(it) }
            binding.eventsRecyclerView.adapter = adapter
            binding.eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        else{
            binding.eventLt.gone()
        }
    }

    private fun observeMatchSummary( ) {
        this@InfoFragment.lifecycleScope.launch {
            viewModel.matchSummaryFlow.collect { result ->
                when (result) {
                    is ApiResult.Loading -> showLoading(true)
                    is ApiResult.Success -> {
                        showLoading(false)
                        val summary = result.data
                        Log.d("MATCH_SUMMARY", "Summary: ${summary}")
                        setupEvents(summary)

                        val compName = summary.competition?.name.takeIf { it != "null" } ?: ""
                        val stageName = summary.competition?.stage_name.takeIf { it != "null" } ?: ""

                        val displayName = when {
                            compName.equals(stageName, ignoreCase = true) -> compName // if both same
                            compName.isNotBlank() && stageName.isNotBlank() -> "$compName - $stageName"
                            compName.isNotBlank() -> compName
                            stageName.isNotBlank() -> stageName
                            else -> "Unknown Competition"
                        }
                        binding.competitionName.setText("${displayName}")
                        binding.venueName.setText("${summary.competition?.country}")
                    }
                    is ApiResult.Error -> {
                        showLoading(false)
//                        showError(result.throwable)
                    }
                }
            }
        }
     }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {
                binding.whowinprobabiltyLtShimmer.visible()
                binding.bottomCardShimmer.visible()
                binding.eventLtShimmer.visible()


                binding.whowinprobabiltyLt.invisible()
                 binding.bottomCard.invisible()
                 binding.eventLt.invisible()
            } else {
                binding.whowinprobabiltyLtShimmer.gone()
                binding.bottomCardShimmer.gone()
                binding.eventLtShimmer.gone()

                binding.whowinprobabiltyLt.visible()
                binding.bottomCard.visible()
                binding.eventLt.visible()

            }
        }?:run{

        }
    }

}