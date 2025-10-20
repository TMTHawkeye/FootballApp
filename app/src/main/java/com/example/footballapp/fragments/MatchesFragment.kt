package com.example.footballapp.fragments

import android.content.Intent
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
import com.example.footballapi.modelClasses.AwayTeam
import com.example.footballapi.modelClasses.HomeTeam
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.teamMatches.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.followingadapters.CompetitionAdapter
import com.example.footballapp.adapters.followingadapters.MatchesAdapter
import com.example.footballapp.databinding.FragmentMatchesBinding
import com.example.footballapp.models.followingmodels.Competition
import com.example.footballapp.models.followingmodels.Match1
import com.example.footballapp.viewmodels.MatchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class MatchesFragment : Fragment() {

    private lateinit var binding: FragmentMatchesBinding
    private  var competitionAdapter: CompetitionAdapter?=null
    private  var matchesAdapter: MatchesAdapter?=null
    private val viewModel: FootballViewModel by activityViewModel()
    private val matchViewModel: MatchViewModel by activityViewModel()

    var selectedCompetition : Stage?=null


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
        matchesAdapter = MatchesAdapter{onMatchSelected->
            val homeTeam = HomeTeam(
                team_id = onMatchSelected.home_team.get(0).team_id,
                abbreviation = onMatchSelected.home_team.get(0).abbreviation,
                logo = onMatchSelected.home_team.get(0).team_badge,
                incident_number = onMatchSelected.home_team.get(0).team_name

            )
            val homeTeamList : MutableList<HomeTeam> = mutableListOf()
            homeTeamList.add(homeTeam)

            val awayTeam = HomeTeam(
                team_id = onMatchSelected.away_team.get(0).team_id,
                abbreviation = onMatchSelected.away_team.get(0).abbreviation,
                logo = onMatchSelected.away_team.get(0).team_badge,
                incident_number = onMatchSelected.away_team.get(0).team_name

            )
            val awayTeamList : MutableList<HomeTeam> = mutableListOf()
            awayTeamList.add(awayTeam)

            val matche = Matche(match_id = onMatchSelected.event_id, home_team = homeTeamList, away_team = awayTeamList, tournamentLogo = selectedCompetition?.badge_url, tournamentName = adjustNameForTournament(selectedCompetition))

            onMatchClicked(matche)
            val intent = Intent(requireContext(), MatchDetailActivity::class.java)
             startActivity(intent)
        }
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
        selectedCompetition = competition
         val allEvents = competition.events.filter { it.stage_id == competition.stage_id }

        if (allEvents.isNotEmpty()) {
            (context as? TeamDetailActivity)?.team?.let {
//                Toast.makeText(binding.root.context, "${it.incident_number} , ${it.team_id} , ${competition.stage_id}}", Toast.LENGTH_SHORT)
//                    .show()

            }
            Log.d("TAG_matchesForTeam", "loadMatchesForCompetition: ${allEvents.size}")
            matchesAdapter?.setMatches(allEvents)
         } else {
            Toast.makeText(requireContext(), "No matches found for ${competition.competition_name}", Toast.LENGTH_LONG).show()
        }
    }


    fun onMatchClicked(matche: Matche) {
        Log.d("TAG_MATCHDATA", "onMatchClicked: ${matche}")
//        matche.tournamentLogo = stageBadge
        matchViewModel.setMatch(matche)
//        matche.match_id?.let { viewModel.loadMatchSummary(it) }
    }

    fun adjustNameForTournament(stage :  Stage?) : String{
        val compName = stage?.competition_name?.takeIf { it != "null" } ?: ""
        val stageName = stage?.stage_name?.takeIf { it != "null" } ?: ""
        val displayName = when {
            compName.equals(stageName, ignoreCase = true) -> compName // if both same
            compName.isNotBlank() && stageName.isNotBlank() -> "$compName - $stageName"
            compName.isNotBlank() -> compName
            stageName.isNotBlank() -> stageName
            else -> "Unknown Competition"
        }

        return displayName
    }


}