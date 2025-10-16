package com.example.footballapi

import android.util.Log
import com.example.footballapi.modelClasses.AllCompetitions.AllCompetitionsResponse
import com.example.footballapi.modelClasses.MatchesRequest
import com.example.footballapi.modelClasses.Stage
import com.example.footballapi.modelClasses.matchLineups.LineupResponse
import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
import com.example.footballapi.modelClasses.matchSummary.MatchSummary
import com.example.footballapi.modelClasses.matchTable.matchTableResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

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
/*

    suspend fun fetchMatchStats(matchId: String) {
        _matchStatsFlow.value = ApiResult.Loading

        try {
            var currentMatchId = matchId

            repeat(3) { // limit redirects
                val responseBody = api.getMatchStats(currentMatchId)
                val responseString = responseBody.string()

                // 🧠 1️⃣ Check if response is JSON or HTML
                val trimmed = responseString.trimStart()
                if (!(trimmed.startsWith("{") || trimmed.startsWith("["))) {
                    Log.w("MATCH_STATS", "Non-JSON response received — likely HTML redirect page.")
                    _matchStatsFlow.value = ApiResult.Error(
                        Exception("Received non-JSON response (HTML).")
                    )
                    return
                }


                // 🧠 2️⃣ Try to parse JSON safely
                val json = JSONObject(responseString)
                val pageProps = json.optJSONObject("pageProps")
                val redirectUrl = pageProps?.optString("__N_REDIRECT")

                if (!redirectUrl.isNullOrEmpty()) {
                    val newMatchId = extractMatchIdFromUrl(redirectUrl)
                    Log.d("MATCH_STATS", "Redirect found → new ID: $newMatchId")
                    currentMatchId = newMatchId
                    return@repeat
                } else {
                    // ✅ No redirect — parse normally
                    val stats = Gson().fromJson(responseString, MatchStatsResponse::class.java)
                    _matchStatsFlow.value = ApiResult.Success(stats)
                    return
                }
            }

            _matchStatsFlow.value = ApiResult.Error(Exception("Too many redirects"))

        } catch (e: Exception) {
            Log.e("MATCH_STATS", "Error fetching stats", e)
            _matchStatsFlow.value = ApiResult.Error(e)
        }
    }
*/



    private fun extractMatchIdFromUrl(url: String): String {
        // Example URL: /en/football/.../1654535/stats/?buildid=ZGFYAJHO8yrt2p4DNoLqT
        val regex = Regex("/(\\d+)/stats")
        return regex.find(url)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Match ID not found in redirect URL: $url")
    }



    //match lineups

    private val _matchLineupFlow = MutableStateFlow<ApiResult<LineupResponse>>(ApiResult.Loading)
    val matchLineupFlow: StateFlow<ApiResult<LineupResponse>> = _matchLineupFlow

    suspend fun fetchMatchLineup(matchId: String) {
        _matchLineupFlow.value = ApiResult.Loading
        try {
            val response = api.getMatchLineups(matchId)
            _matchLineupFlow.value = ApiResult.Success(response)
        } catch (e: Exception) {
            _matchLineupFlow.value = ApiResult.Error(e)
        }
    }



    //match table

    private val _matchTableFlow = MutableStateFlow<ApiResult<matchTableResponse>>(ApiResult.Loading)
    val matchTableFlow: StateFlow<ApiResult<matchTableResponse>> = _matchTableFlow

    suspend fun fetchMatchTable(matchId: String) {
        _matchTableFlow.value = ApiResult.Loading
        try {
            val response = api.getMatchTable(matchId)
            _matchTableFlow.value = ApiResult.Success(response)
        } catch (e: Exception) {
            _matchTableFlow.value = ApiResult.Error(e)
        }
    }


   // all Competitions

  private val _allCompetitionsFlow = MutableStateFlow<ApiResult<AllCompetitionsResponse>>(ApiResult.Loading)
    val allCompetitionsFlow: StateFlow<ApiResult<AllCompetitionsResponse>> = _allCompetitionsFlow

    suspend fun fetchAllCompetitions() {
        _allCompetitionsFlow.value = ApiResult.Loading
        try {
            val response = api.getAllCompetitions()
            _allCompetitionsFlow.value = ApiResult.Success(response)
        } catch (e: Exception) {
            _allCompetitionsFlow.value = ApiResult.Error(e)
        }
    }

}
