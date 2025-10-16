package com.example.footballapi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapi.modelClasses.AllCompetitions.AllCompetitionsResponse
import com.example.footballapi.modelClasses.Stage
import com.example.footballapi.modelClasses.matchLineups.LineupResponse
import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapi.modelClasses.matchTable.matchTableResponse
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


}
