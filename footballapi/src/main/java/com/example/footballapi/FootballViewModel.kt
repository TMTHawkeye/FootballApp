package com.example.footballapi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.footballapi.modelClasses.AllCompetitions.AllCompetitionsResponse
import com.example.footballapi.modelClasses.Stage
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponse
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapi.modelClasses.matchLineups.LineupResponse
import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapi.modelClasses.matchTable.matchTableResponse
import com.example.footballapi.modelClasses.teamMatches.TeamMatchesResponse
import com.example.footballapi.modelClasses.youtube_shorts.YouTubeShortsResponseItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FootballViewModel(
    private val repo: FootballRepository
) : ViewModel() {

    val matchesFlow: StateFlow<ApiResult<List<Stage>>>
        get() = repo.matchesFlow


      fun loadMatchesWithStages(date: String? = getTodayDate(), page: Int=1) {
        viewModelScope.launch {
            Log.d("ApiResult_Tag", "loadMatches: date: $date , page : $page")
            date?.let { repo.fetchAllMatchesWithStages(it, page) }?:run{
                repo.fetchAllMatchesWithStages(getTodayDate(), page)
            }
        }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }


    val matchSummaryFlow: StateFlow<ApiResult<MatchSummary>> get() = repo.matchSummaryFlow

    fun loadMatchSummary(matchId: String) {
        viewModelScope.launch {
            repo.fetchMatchSummary(matchId)
        }
    }



    val matchStatsFlow: StateFlow<ApiResult<MatchStatsResponse>> get() = repo.matchStatsFlow

    fun loadMatchStats(matchId: String) {
        viewModelScope.launch {
            repo.fetchMatchStats(matchId)
        }
    }


    val matchLineupFlow: StateFlow<ApiResult<LineupResponse>> get() = repo.matchLineupFlow

    fun loadMatchLineups(matchId: String) {
        viewModelScope.launch {
            repo.fetchMatchLineup(matchId)
        }
    }


    val matchTableFlow: StateFlow<ApiResult<matchTableResponse>> get() = repo.matchTableFlow

    fun loadMatchTable(matchId: String) {
        viewModelScope.launch {

            repo.fetchMatchTable(matchId)
        }
    }


    val allCompetitionsFlow: StateFlow<ApiResult<AllCompetitionsResponse>> get() = repo.allCompetitionsFlow

    fun loadAllCompetitions() {
        viewModelScope.launch {
            repo.fetchAllCompetitions()
        }
    }



    val teamMatchesFlow: StateFlow<ApiResult<TeamMatchesResponse>> get() = repo.teamMatchesFlow

    fun loadTeamMatches(teamId : String) {
        viewModelScope.launch {
            repo.fetchTeamMatches(teamId)
        }
    }



    val teamStandingsFlow: StateFlow<ApiResult<TeamMatchesResponse>> get() = repo.teamStandingsFlow

    fun loadTeamStandings(teamName :String,teamId :String,stageId :String) {
        viewModelScope.launch {
            repo.fetchTeamStandings(teamName, teamId, stageId)
        }
    }



    val latestNewsFlow: StateFlow<ApiResult<LatestNewsResponse>> get() = repo.latestNewsFlow

    fun loadLatestNews(pageNo :String) {
        viewModelScope.launch {
            repo.fetchLatestNews(pageNo)
        }
    }


    val latestPagedNewsFlow: Flow<PagingData<LatestNewsResponseItem>> =
        repo.getPagedNews().cachedIn(viewModelScope)


    val youtubeShortsFlow: StateFlow<ApiResult<List<YouTubeShortsResponseItem>>> get() = repo.youtubeShortsFlow

    fun loadYoutubeShorts() {
        viewModelScope.launch {
            repo.fetchYoutubeShorts()
        }
    }



    fun clearMatchData(){
        repo.clearMatchData()
    }


    fun clearTeamDetailsData(){
        repo.clearTeamDetilsData()
    }



}
