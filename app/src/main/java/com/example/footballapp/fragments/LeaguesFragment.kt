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
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.ApiResult
import com.example.footballapi.FootballViewModel
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.LEAGUE_ID
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.LeagueDetailActivity
import com.example.footballapp.adapters.followingadapters.FollowedLeaguesAdapter
import com.example.footballapp.adapters.followingadapters.FollowingTeamsAdapter
import com.example.footballapp.adapters.followingadapters.SuggestedLeaguesAdapter
import com.example.footballapp.databinding.FragmentLeaguesBinding
import com.example.footballapp.models.Team
import com.example.footballapp.models.followingmodels.Team1
import com.example.footballapp.utils.LeagueListType
import com.example.footballapp.viewmodels.FollowViewModel
import com.example.footballapp.viewmodels.TeamViewmodel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import kotlin.getValue


class LeaguesFragment : Fragment() {

    private lateinit var binding: FragmentLeaguesBinding

    //    private  var followingLeaguesAdapter: SuggestedLeaguesAdapter?=null
    private var suggestedLeaguesAdapter: SuggestedLeaguesAdapter? = null

    private val viewModel: FootballViewModel by activityViewModel()
    private val followViewModel: FollowViewModel by activityViewModel()

    private val teamViewModel: TeamViewmodel by activityViewModel()


    private var currentPage = 0
    private val pageSize = 100
    private var allLeagues: List<Stage> = emptyList()
    private var followedLeaguesList: List<Stage> = emptyList()
    private var moreLeaguesList: MutableList<Stage> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLeaguesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()

        observeCompetitions()


    }

    private fun setupAdapters() {
        // ✅ Following leagues
//        followingLeaguesAdapter = SuggestedLeaguesAdapter(
//            mutableListOf(),
//            onFollowClick = { league ->
//                followViewModel.toggleFollowLeague(league.stage_id) // unfollow
//            },
//            onItemClick = {league->
//                     binding.root.context.startActivity(Intent(binding.root.context,
//                         LeagueDetailActivity::class.java))
//
//            },
//            listType = LeagueListType.FOLLOWING
//        )
//
//        binding.rvFollowingTeams.apply {
//            adapter = followingLeaguesAdapter
//            layoutManager = LinearLayoutManager(requireContext())
//        }

        // ✅ Suggested leagues
        suggestedLeaguesAdapter = SuggestedLeaguesAdapter(
            onFollowClick = { league ->
                followViewModel.toggleFollowLeague(league.stage_id) // toggle follow
            },
            onItemClick = { league ->
                teamViewModel.setLeague(league)
                binding.root.context.startActivity(
                    Intent(
                        binding.root.context,
                        LeagueDetailActivity::class.java
                    )
                )
//                    .putExtra(LEAGUE_ID, league.stage_id))
            },
//            listType = LeagueListType.SUGGESTED
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


    private fun navigateToLeagueDetail(league: Stage) {
        val intent = Intent(requireContext(), LeagueDetailActivity::class.java).apply {
//            putExtra("league_id", league.id)
//            putExtra("league_name", league.name)
        }
        startActivity(intent)
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
                combine(
                    viewModel.allCompetitionsFlow,
                    followViewModel.followedLeagues
                ) { matchesResult, followedIds ->
                    matchesResult to followedIds
                }.collectLatest { (result, followedIds) ->
//                    when (result) {
//
//                    }
//                }
//            }
//        }
//        this@LeaguesFragment.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.allCompetitionsFlow.collect { result ->
                    when (result) {
                        is ApiResult.Loading -> {

                            showLoading(true)
                        }

                        is ApiResult.Success -> {

//                            showLoading(false)
//                            val stages = result.data.stages
//                            suggestedLeaguesAdapter?.updateLeagues(stages)
//                            extractFollowedLeagues(stages)
//                            Log.d("AllCompetitions", "${stages.size}")


                            showLoading(false)
                            allLeagues = result.data.stages
                            Log.d("TAG_leagues", "observeCompetitions: ${allLeagues.size}")

                            //                                extractUniqueTeams(result.data)
//                                extractFollowedLeagues(stages) {
                            //                                    allLeagues = it


                            val (followed, more) = allLeagues.partition {
                                followedIds.contains(
                                    it.stage_id
                                )
                            }

                            followedLeaguesList = followed
                            moreLeaguesList.clear()
                            moreLeaguesList.addAll(more)
                            Log.d("TAG_leagues", "observeCompetitions3: ${moreLeaguesList.size}")

                            currentPage = 0
                            loadNextPage()
//                                }

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
            suggestedLeaguesAdapter?.setData(followedLeaguesList, nextPage)
        } else {
            suggestedLeaguesAdapter?.addMoreLeagues(nextPage)
        }

        currentPage++
    }

    suspend fun extractFollowedLeagues(stages: List<Stage>, callback: (List<Stage>) -> Unit) {
        followViewModel.followedLeagues.collect { followedIds ->
            val followedStages = stages.filter { followedIds.contains(it.competition_id) }
            Log.d("TAG_leagues", "observeCompetitions1: ${followedStages.size}")

            callback.invoke(followedStages)

//            suggestedLeaguesAdapter?.updateFollowedIds(followedIds)
////            followingLeaguesAdapter?.updateFollowedIds(followedIds)
////             followingLeaguesAdapter?.updateLeagues(followedStages)
//
//            Log.d("FollowedLeagues", "Count: ${followedStages.size}")
//            binding.textView6.text = setSpannedFollwoingCount(followedStages.size)
//            if(followedStages.size==0){
//                binding.constraintLayout6.gone()
//            }
//            else{
//                binding.constraintLayout6.visible()
//
//            }
        }
    }

    private fun setSpannedFollwoingCount(followedStages: Int): SpannableString {
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

