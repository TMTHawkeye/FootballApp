package com.example.footballapp.fragments.searchFragments

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.TeamDetailActivity
import com.example.footballapp.adapters.TeamsAdapter
import com.example.footballapp.databinding.FragmentSearchTeamsBinding
import com.example.footballapp.models.Team
import com.example.footballapp.viewmodels.FollowTeamViewModel
import com.example.footballapp.viewmodels.SearchSharedViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.collections.containsKey
import kotlin.collections.forEach
import kotlin.collections.plus
import kotlin.getValue

class SearchTeamsFragment : Fragment() {
    lateinit var binding : FragmentSearchTeamsBinding


    private val viewModel: FootballViewModel by activityViewModel()
//    private val followViewModel: FollowTeamViewModel by activityViewModel()
    private val teamViewModel: TeamViewmodel by activityViewModel()

    private var isLoadingNextPage = false

    private lateinit var teamsAdapter: TeamsAdapter
    private var currentPage = 0
    private val pageSize = 100
    private var allTeams: List<Team> = emptyList()
//    private var followedTeamsList: List<Team> = emptyList()
    private var moreTeamsList: MutableList<Team> = mutableListOf()


    private var fullTeamList: List<Team> = emptyList()


    private val sharedViewModel: SearchSharedViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =FragmentSearchTeamsBinding.inflate(layoutInflater,container,false)
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
            false,
            onItemClick = { navigateToTeamDetail(it) },
            onFollowToggle = {   }
        )

        binding.rvTeams.apply {
            adapter = teamsAdapter
            layoutManager = LinearLayoutManager(requireContext())

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
                viewModel.matchesFlow.collectLatest { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            showLoading(false)

                            allTeams = withContext(Dispatchers.IO) {
                                extractUniqueTeams(result.data)
                            }

                            // Just show all teams directly
                            moreTeamsList.clear()
                            moreTeamsList.addAll(allTeams)

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
            fullTeamList =  nextPage
            teamsAdapter.setData(emptyList(), nextPage)
        } else {
            fullTeamList =   nextPage
            teamsAdapter.addMoreTeams(nextPage)
        }

        currentPage++
    }





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




    private fun observeSearchQuery() {
        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            filterTeams(query)
        }
    }

    private fun filterTeams(query: String) {
        if (query.isBlank()) {
            // ✅ Restore default grouped view
            teamsAdapter.setData(emptyList(), moreTeamsList)
        } else {
            // ✅ Filtered flat list (no headers)
            val filtered = fullTeamList.filter {
                it.incident_number?.contains(query, ignoreCase = true) == true
            }
            teamsAdapter.filterList(filtered)
        }
    }


}