package com.example.footballapi

import android.util.Log
import com.example.footballapi.modelClasses.MatchesRequest
import com.example.footballapi.modelClasses.Stage
import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FootballRepository(private val api: FootballApiService) {

//get all matches
    private val _matchesFlow =
        MutableStateFlow<ApiResult<List<Stage>>>(ApiResult.Loading)
    val matchesFlow: StateFlow<ApiResult<List<Stage>>> = _matchesFlow

    suspend fun fetchAllMatchesWithStages(date: String, page: Int) {
        _matchesFlow.value = ApiResult.Loading
        try {
            Log.d("TAG_fetchAllMatches", "fetchAllMatches: called")
            val response = api.getAllMatches(MatchesRequest(date, page))
            _matchesFlow.value = ApiResult.Success(response.Stages ?: emptyList())
        } catch (e: Exception) {
            _matchesFlow.value = ApiResult.Error(e)
        }
    }


    //match summary

    private val _matchSummaryFlow = MutableStateFlow<ApiResult<MatchSummary>>(ApiResult.Loading)
    val matchSummaryFlow: StateFlow<ApiResult<MatchSummary>> = _matchSummaryFlow

    suspend fun fetchMatchSummary(matchId: String) {
        _matchSummaryFlow.value = ApiResult.Loading
        try {
            val response = api.getMatchSummary(matchId)
            _matchSummaryFlow.value = ApiResult.Success(response)
        } catch (e: Exception) {
            _matchSummaryFlow.value = ApiResult.Error(e)
        }
    }

    //match stats

    private val _matchStatsFlow = MutableStateFlow<ApiResult<MatchStatsResponse>>(ApiResult.Loading)
    val matchStatsFlow: StateFlow<ApiResult<MatchStatsResponse>> = _matchStatsFlow

    suspend fun fetchMatchStats(matchId: String) {
        _matchStatsFlow.value = ApiResult.Loading
        try {
            val response = api.getMatchStats(matchId)
            _matchStatsFlow.value = ApiResult.Success(response)
        } catch (e: Exception) {
            _matchStatsFlow.value = ApiResult.Error(e)
        }
    }

}
