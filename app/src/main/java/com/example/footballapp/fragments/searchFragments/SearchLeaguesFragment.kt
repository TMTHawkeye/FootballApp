package com.example.footballapp.fragments.searchFragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.adapters.followingadapters.SuggestedLeaguesAdapter
import com.example.footballapp.databinding.FragmentSearchLeaguesBinding
import com.example.footballapp.viewmodels.FollowViewModel
import com.example.footballapp.viewmodels.SearchSharedViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue

class SearchLeaguesFragment : Fragment() {

    lateinit var binding : FragmentSearchLeaguesBinding

    private var suggestedLeaguesAdapter: SuggestedLeaguesAdapter? = null

    private val viewModel: FootballViewModel by activityViewModel()

    private val teamViewModel: TeamViewmodel by activityViewModel()
    private val sharedViewModel: SearchSharedViewModel by activityViewModel()


    private var fullTeamList: List<Stage> = emptyList()


    private var currentPage = 0
    private val pageSize = 100
    private var allLeagues: List<Stage> = emptyList()
     private var moreLeaguesList: MutableList<Stage> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentSearchLeaguesBinding.inflate(layoutInflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()

        observeCompetitions()
        observeSearchQuery()

    }

    private fun setupAdapters() {
        suggestedLeaguesAdapter = SuggestedLeaguesAdapter(
            false,
            onFollowClick = { league ->
             },
            onItemClick = { league ->
                teamViewModel.setLeague(league)
                binding.root.context.startActivity(
                    Intent(
                        binding.root.context,
                        LeagueDetailActivity::class.java
                    )
                )
            },
        )

        binding.rvLeagues.apply {
            adapter = suggestedLeaguesAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (totalItemCount <= lastVisibleItem + 10) { // threshold
                        loadNextPage()
                    }
                }
            })
        }
    }

    private fun showLoading(show: Boolean?) {
        Log.d(ApiResultTAG, "showLoading: $show")

        show?.let {
            if (show) {
                binding.rvLeaguesShimmer.visible()
                binding.rvLeagues.invisible()


            } else {
                binding.rvLeaguesShimmer.invisible()
                binding.rvLeagues.visible()
            }
        } ?: run {
        }
    }

    private fun observeCompetitions() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.allCompetitionsFlow.collectLatest { result ->
                    when (result) {
                        is ApiResult.Loading -> {
                            showLoading(true)
                        }

                        is ApiResult.Success -> {
                            showLoading(false)
                            allLeagues = result.data.stages
                            Log.d("TAG_leagues", "observeCompetitions: ${allLeagues.size}")

                            // Directly use all leagues (no partitioning)
                            moreLeaguesList.clear()
                            moreLeaguesList.addAll(allLeagues)

                            currentPage = 0
                            loadNextPage()
                        }

                        is ApiResult.Error -> {
                            showLoading(null)
                        }
                    }
                }
            }
        }

        viewModel.loadAllCompetitions()
    }



    private fun loadNextPage() {
        val start = currentPage * pageSize
        val end = minOf(start + pageSize, moreLeaguesList.size)

        if (start >= end) return // no more pages

        val nextPage = moreLeaguesList.subList(start, end)
        if (currentPage == 0) {
            fullTeamList =  nextPage

            suggestedLeaguesAdapter?.setData(emptyList(), nextPage)
        } else {
            fullTeamList =   nextPage

            suggestedLeaguesAdapter?.addMoreLeagues(nextPage)
        }

        currentPage++
    }





    private fun observeSearchQuery() {
        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            filterTeams(query)
        }
    }

    private fun filterTeams(query: String) {
        if (query.isBlank()) {
            // ✅ Restore default grouped view
            suggestedLeaguesAdapter?.setData(emptyList(), moreLeaguesList)
        } else {
            // ✅ Filtered flat list (no headers)
            val filtered = fullTeamList.filter {
                it.competition_name?.contains(query, ignoreCase = true) == true
            }
            suggestedLeaguesAdapter?.filterList(filtered)
        }
    }



}