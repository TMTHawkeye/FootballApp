package com.example.footballapp.fragments.leagueDetailsFragments

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
import com.example.footballapi.modelClasses.HomeTeam
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.adapters.LeagueMatchesAdapter
import com.example.footballapp.adapters.followingadapters.MatchesAdapter
import com.example.footballapp.databinding.FragmentLeagueMatchesBinding
import com.example.footballapp.viewmodels.MatchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class LeagueMatchesFragment : Fragment() {
    lateinit var binding : FragmentLeagueMatchesBinding

    private  var matchesAdapter: LeagueMatchesAdapter?=null
    private val viewModel: FootballViewModel by activityViewModel()
    private val matchViewModel: MatchViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeagueMatchesBinding.inflate(layoutInflater,container,false)

        return binding?.root
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()

        observeMatches()

    }

    private fun setupAdapters() {


        // Matches vertical RecyclerView
        matchesAdapter = LeagueMatchesAdapter{onMatchSelected->
            val homeTeam = HomeTeam(
//                team_id = onMatchSelected.home_team,
//                abbreviation = onMatchSelected.home_team.get(0).abbreviation,
//                logo = onMatchSelected.home_team.get(0).team_badge,
                incident_number = onMatchSelected.home_team

            )
            val homeTeamList : MutableList<HomeTeam> = mutableListOf()
            homeTeamList.add(homeTeam)

            val awayTeam = HomeTeam(
//                team_id = onMatchSelected.away_team.get(0).team_id,
//                abbreviation = onMatchSelected.away_team.get(0).abbreviation,
//                logo = onMatchSelected.away_team.get(0).team_badge,
                incident_number = onMatchSelected.away_team

            )
            val awayTeamList : MutableList<HomeTeam> = mutableListOf()
            awayTeamList.add(awayTeam)

            val matche = Matche(match_id = onMatchSelected.match_id, home_team = homeTeamList, away_team = awayTeamList/*, tournamentLogo = selectedCompetition?.badge_url, tournamentName = adjustNameForTournament(selectedCompetition)*/)

            onMatchClicked(matche)
            val intent = Intent(requireContext(), MatchDetailActivity::class.java)
            startActivity(intent)
        }
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatches.adapter = matchesAdapter
    }

    private fun observeMatches() {
        this@LeagueMatchesFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.leagueMatchesFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            val matches = result.data.fixtures
                            Log.d("TAG_LEAGUE_MATCHES", "observeMatches: ${matches.size}")
                            if (matches.isEmpty()) {
                                showLoading(null)

                             } else {
                                showLoading(false)

                                binding.rvMatches.visible()
                                matchesAdapter?.setMatches(matches)
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
        Log.d(ApiResultTAG, "showLoading league matches: $show")

        show?.let {
            if (show) {
                binding.rvMatchesShimmer.visible()

                binding.rvMatches.invisible()

            } else {
                binding.rvMatches.visible()

                binding.rvMatchesShimmer.gone()

            }
        }?:run{
            Toast.makeText(binding.root.context, "No matches for league", Toast.LENGTH_SHORT).show()

            binding.rvMatches.gone()

            binding.rvMatchesShimmer.gone()
        }
    }

    fun onMatchClicked(matche: Matche) {
        Log.d("TAG_MATCHDATA", "onMatchClicked: ${matche}")
//        matche.tournamentLogo = stageBadge
        matchViewModel.setMatch(matche)
//        matche.match_id?.let { viewModel.loadMatchSummary(it) }
    }


}