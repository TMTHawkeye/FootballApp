package com.example.footballapp.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.HomeTeam
import com.example.footballapi.modelClasses.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.followingadapters.FollowingTeamsAdapter
import com.example.footballapp.adapters.followingadapters.SuggestedTeamsAdapter
import com.example.footballapp.databinding.FragmentTeamsBinding
import com.example.footballapp.models.Team
import com.example.footballapp.utils.LeagueListType
import com.example.footballapp.viewmodels.FollowTeamViewModel
import com.example.footballapp.viewmodels.FollowViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.getValue

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding
    private  var followingTeamsAdapter: SuggestedTeamsAdapter?=null
    private var suggestedTeamsAdapter: SuggestedTeamsAdapter? = null

    private val viewModel: FootballViewModel by activityViewModel()
    private val followViewModel: FollowTeamViewModel by activityViewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTeamsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSuggestedTeamsAdapter()
//        loadData()


        observeCompetitions()
    }

    private fun setupSuggestedTeamsAdapter() {
        // Following Teams RecyclerView
//        followingTeamsAdapter = FollowingTeamsAdapter() { team ->
//            navigateToTeamDetail(team)
//        }
//        binding.rvFollowingTeams.adapter = followingTeamsAdapter
//        binding.rvFollowingTeams.layoutManager = LinearLayoutManager(requireContext())


        followingTeamsAdapter = SuggestedTeamsAdapter(mutableListOf(),
            onItemClick = { team -> // For item click (navigate to detail)
                navigateToTeamDetail(team)
            },
            onFollowClick = { team ->
                team.team_id?.let { followViewModel.toggleFollowTeam(it) } // toggle follow
            } ,
            listType = LeagueListType.FOLLOWING
        )
        binding.rvFollowingTeams.adapter = followingTeamsAdapter
        binding.rvFollowingTeams.layoutManager = LinearLayoutManager(requireContext())


        suggestedTeamsAdapter = SuggestedTeamsAdapter(mutableListOf(),
            onItemClick = { team -> // For item click (navigate to detail)
                navigateToTeamDetail(team)
            },
            onFollowClick = { team ->
                team.team_id?.let { followViewModel.toggleFollowTeam(it) } // toggle follow
            } ,
            listType = LeagueListType.SUGGESTED
        )
        binding.rvSuggestedTeams.adapter = suggestedTeamsAdapter
        binding.rvSuggestedTeams.layoutManager = LinearLayoutManager(requireContext())
        
    }




    private fun navigateToTeamDetail(team: Team) {
        val intent = Intent(requireContext(), TeamDetailActivity::class.java).apply {
//            putExtra("team_id", team.team_id)
//            putExtra("team_name", team.name)
        }
        startActivity(intent)
    }

    private fun followTeam(team: Team) {
        // Implement follow functionality
        Toast.makeText(requireContext(), "Followed ${team.incident_number}", Toast.LENGTH_SHORT).show()
    }



    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {


            } else {

            }
        }?:run{
        }
    }



    private fun observeCompetitions() {
        this@TeamsFragment.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.matchesFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

                            showLoading(false)
                            val stages = result.data
                            val uniqueTeams = withContext(Dispatchers.Default) {
                                extractUniqueTeams(stages)
                            }
                             Log.d("UniqueTeams", "Found ${uniqueTeams.size} unique teams")

                            suggestedTeamsAdapter?.updateTeams(uniqueTeams)
                            extractFollowedTeams(uniqueTeams)
                        }

                        is ApiResult.Error -> {

                            showLoading(null)

                        }
                    }
                }
            }
        }
    }

    fun extractUniqueTeams(stages: List<Stage>): List<Team> {
        val teamMap = mutableMapOf<String, Team>() // key: team_id

        for (stage in stages) {
            stage.matches?.forEach { match ->
                // Combine home + away teams safely
                val allTeams = (match.home_team ?: emptyList()) + (match.away_team ?: emptyList())

                allTeams.forEach { t ->
                    if (!teamMap.containsKey(t.team_id)) {
                        teamMap[t.team_id] = Team(
                            team_id = t.team_id,
                            abbreviation = t.abbreviation,
                            incident_number = t.incident_number,
                            logo = t.logo,
                            news_tag = t.news_tag,
                            primary_color = t.primary_color,
                            secondary_color = t.secondary_color
                        )
                    }
                }
            }
        }

        return teamMap.values.toList()
    }


    suspend fun extractFollowedTeams(stages : List<Team>){
        followViewModel.followedTeams.collect { followedIds ->
            val followedTeams = stages.filter { followedIds.contains(it.team_id) }

            suggestedTeamsAdapter?.updateFollowedIds(followedIds)
            followingTeamsAdapter?.updateFollowedIds(followedIds)
            followingTeamsAdapter?.updateTeams(followedTeams)

            Log.d("FollowedLeagues", "Count: ${followedTeams.size}")
            binding.textView6.text = setSpannedFollwoingCount(followedTeams.size)
            if(followedTeams.size==0){
                binding.constraintLayout6.gone()
            }
            else{
                binding.constraintLayout6.visible()

            }
        }
    }

    private fun setSpannedFollwoingCount(followedStages: Int)  : SpannableString {
        val context = binding.root.context
        val baseText = context.getString(R.string.followingss) // e.g. "Followings: "
        val countText = followedStages.toString()

        val fullText = "$baseText $countText"

        val spannable = SpannableString(fullText)
        val start = baseText.length
        val end = fullText.length

        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.green_color)),
            start,
            end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable

    }

}

//data class Team(val name: String, val imageRes: Int, val id: String)