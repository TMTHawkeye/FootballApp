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
import com.example.footballapi.modelClasses.teamMatches.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.MatchesAdapter
import com.example.footballapp.databinding.FragmentMatchesBinding
import com.example.footballapp.models.followingmodels.Competition
import com.example.footballapp.models.followingmodels.Match1
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class MatchesFragment : Fragment() {

    private lateinit var binding: FragmentMatchesBinding
    private  var competitionAdapter: CompetitionAdapter?=null
    private  var matchesAdapter: MatchesAdapter?=null
    private val viewModel: FootballViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setCompetitionAdapter()

         observeMatches()
     }

    private fun setupAdapters() {


        // Matches vertical RecyclerView
        matchesAdapter = MatchesAdapter()
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatches.adapter = matchesAdapter
    }


    private fun observeMatches() {
        this@MatchesFragment.lifecycleScope.launch {
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

                             if (stages.isNotEmpty() || stages.size!=0) {
                                competitionAdapter?.updateSelectedPosition(0)

                                // 3️⃣ Load matches for the first competition
                                loadMatchesForCompetition(stages[0])
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
                binding.rvMatchesShimmer.visible()
                binding.rvCompetitionsShimmer.visible()

                binding.rvMatches.invisible()
                binding.rvCompetitions.invisible()

            } else {
                binding.rvMatches.visible()
                binding.rvCompetitions.visible()

                binding.rvMatchesShimmer.gone()
                binding.rvCompetitionsShimmer.gone()

            }
        }?:run{
        }
    }

    fun setCompetitionAdapter() {
        competitionAdapter = CompetitionAdapter(ArrayList()) { competition, position ->
            competitionAdapter?.updateSelectedPosition(position)
            loadMatchesForCompetition(competition)
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


    private fun loadMatchesForCompetition(competition: Stage) {
         val allEvents = competition.events.filter { it.stage_id == competition.stage_id }

        if (allEvents.isNotEmpty()) {
            Log.d("TAG_matchesForTeam", "loadMatchesForCompetition: ${allEvents.size}")
            matchesAdapter?.setMatches(allEvents)
         } else {
            Toast.makeText(requireContext(), "No matches found for ${competition.competition_name}", Toast.LENGTH_SHORT).show()
        }
    }




}