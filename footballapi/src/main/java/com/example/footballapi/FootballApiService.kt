package com.example.footballapi

 import com.example.footballapi.modelClasses.AllCompetitions.AllCompetitionsResponse
 import com.example.footballapi.modelClasses.AllMatches
 import com.example.footballapi.modelClasses.MatchesRequest
 import com.example.footballapi.modelClasses.matchLineups.LineupResponse
 import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
 import com.example.footballapi.modelClasses.matchSummary.MatchSummary
 import com.example.footballapi.modelClasses.matchTable.matchTableResponse
 import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FootballApiService {

    // 7.1 Fetch All Matches
    @POST("api/football/")
    suspend fun getAllMatches(
        @Body request: MatchesRequest
    ): AllMatches



    @GET("api/football/match/{match_id}/")
    suspend fun getMatchSummary(
        @Path("match_id") matchId: String
    ): MatchSummary


    @GET("api/football/match/{match_id}/stats")
    suspend fun getMatchStats(
        @Path("match_id") matchId: String
    ): MatchStatsResponse

    @GET("api/football/match/{match_id}/lineups")
    suspend fun getMatchLineups(
        @Path("match_id") matchId: String
    ): LineupResponse


    @GET("api/football/match/{match_id}/table/")
    suspend fun getMatchTable(
        @Path("match_id") matchId: String
    ): matchTableResponse



    @GET("/api/football/competitions/")
    suspend fun getAllCompetitions(
    ): AllCompetitionsResponse


}
