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
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.HomeTeam
import com.example.footballapi.modelClasses.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.TEAM_ID
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.TeamsAdapter
import com.example.footballapp.adapters.followingadapters.FollowingTeamsAdapter
import com.example.footballapp.adapters.followingadapters.SuggestedTeamsAdapter
import com.example.footballapp.databinding.FragmentTeamsBinding
import com.example.footballapp.models.Team
 import com.example.footballapp.utils.LeagueListType
import com.example.footballapp.viewmodels.FollowTeamViewModel
import com.example.footballapp.viewmodels.FollowViewModel
import com.example.footballapp.viewmodels.MatchViewModel
import com.example.footballapp.viewmodels.SearchSharedViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.collections.forEach
import kotlin.getValue

class TeamsFragment : Fragment() {

    private lateinit var binding: FragmentTeamsBinding

    private val viewModel: FootballViewModel by activityViewModel()
    private val followViewModel: FollowTeamViewModel by activityViewModel()
    private val teamViewModel: TeamViewmodel by activityViewModel()

    private var isLoadingNextPage = false

    private lateinit var teamsAdapter: TeamsAdapter
    private var currentPage = 0
    private val pageSize = 100
    private var allTeams: List<Team> = emptyList()
    private var followedTeamsList: List<Team> = emptyList()
    private var moreTeamsList: MutableList<Team> = mutableListOf()


     private var fullTeamList: List<Team> = emptyList()


    private val sharedViewModel: SearchSharedViewModel by activityViewModel()


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
//        observeCompetitions()
//        observeFollowedTeams()
        observeTeamsData()
        observeSearchQuery()
    }

    private fun setupSuggestedTeamsAdapter() {
        teamsAdapter = TeamsAdapter(
            onItemClick = { navigateToTeamDetail(it) },
            onFollowToggle = { it.team_id?.let { leagueId -> followViewModel.toggleFollowTeam(leagueId) } }
        )

        binding.rvTeams.apply {
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(requireContext())
//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                    val totalItemCount = layoutManager.itemCount
//                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
//
//                    if (totalItemCount <= lastVisibleItem + 10) { // threshold
//                        loadNextPage()
//                    }
//                }
//            })\

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy <= 0) return // ignore upward scrolls

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!isLoadingNextPage && lastVisibleItem + 10 >= totalItemCount) {
                        isLoadingNextPage = true
                        loadNextPage()
                    }
                }
            })

        }
    }

    private fun navigateToTeamDetail(team: Team) {
        teamViewModel.setTeam(team)
        val intent = Intent(requireContext(), TeamDetailActivity::class.java)
        startActivity(intent)
    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {
                binding.rvTeamsShimmer.visible()
                binding.rvTeams.invisible()


            } else {
                binding.rvTeamsShimmer.invisible()
                binding.rvTeams.visible()
            }
        }?:run{
        }
    }


    private fun observeTeamsData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    viewModel.matchesFlow,
                    followViewModel.followedTeams
                ) { matchesResult, followedIds ->
                    matchesResult to followedIds
                }.collectLatest { (result, followedIds) ->
                    when (result) {
                        is ApiResult.Success -> {
                            showLoading(false)
                            allTeams = withContext(Dispatchers.IO) {
                                extractUniqueTeams(result.data)
                            }

                            val (followed, more) = allTeams.partition { followedIds.contains(it.team_id) }

                            followedTeamsList = followed
                            moreTeamsList.clear()
                            moreTeamsList.addAll(more)

                            currentPage = 0
                            loadNextPage()
                        }

                        is ApiResult.Loading -> showLoading(true)
                        is ApiResult.Error -> showLoading(null)
                    }
                }
            }
        }
    }



    private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, moreTeamsList.size)

        if (start >= end) return // no more pages

        val nextPage = moreTeamsList.subList(start, end)
        if (currentPage == 0) {
            fullTeamList = followedTeamsList + nextPage
            teamsAdapter.setData(followedTeamsList, nextPage)
        } else {
            fullTeamList = followedTeamsList + nextPage
            teamsAdapter.addMoreTeams(nextPage)
        }

        currentPage++
    }

 /*   private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, moreTeamsList.size)

        if (start >= end) {
            isLoadingNextPage = false
            return // no more pages
        }

        val nextPage = moreTeamsList.subList(start, end)
        if (currentPage == 0) {
            teamsAdapter.setData(followedTeamsList, nextPage)
        } else {
            teamsAdapter.addMoreTeams(nextPage)
        }

        currentPage++
        isLoadingNextPage = false
    }*/




    fun extractUniqueTeams(stages: List<Stage>): List<Team> {
        val teamMap = mutableMapOf<String, Team>() // key: team_id

        for (stage in stages) {
            stage.matches?.forEach { match ->
                // Combine home + away teams safely
                val allTeams = (match.home_team ?: emptyList()) + (match.away_team ?: emptyList())

                allTeams.forEach { t ->
                    if (!teamMap.containsKey(t.team_id)) {
                        t.team_id?.let {
                            teamMap[t.team_id!!] = Team(
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
        }

        return teamMap.values.toList()
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


    private fun observeSearchQuery() {
        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            filterTeams(query)
        }
    }

    private fun filterTeams(query: String) {
        if (query.isBlank()) {
            // ✅ Restore default grouped view
            teamsAdapter.setData(followedTeamsList, moreTeamsList)
        } else {
            // ✅ Filtered flat list (no headers)
            val filtered = fullTeamList.filter {
                it.incident_number?.contains(query, ignoreCase = true) == true
            }
            teamsAdapter.filterList(filtered)
        }
    }


}

