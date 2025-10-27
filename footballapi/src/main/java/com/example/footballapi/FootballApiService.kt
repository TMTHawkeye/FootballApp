package com.example.footballapi

 import com.example.footballapi.leaguePlayerStats.LeagueTopScorerResponse
 import com.example.footballapi.modelClasses.AllCompetitions.AllCompetitionsResponse
 import com.example.footballapi.modelClasses.AllMatches
 import com.example.footballapi.modelClasses.MatchesRequest
 import com.example.footballapi.modelClasses.TeamTable.TeamTableResponse
 import com.example.footballapi.modelClasses.highlights.GeneralHighlightsResponse
 import com.example.footballapi.modelClasses.highlights.TeamHighlightsResponse
 import com.example.footballapi.modelClasses.latestNews.LatestNewsResponse
 import com.example.footballapi.modelClasses.leagueMatches.LeagueMatchesResponse
 import com.example.footballapi.modelClasses.leagueStandings.LeagueStandingsResponse
 import com.example.footballapi.modelClasses.matchLineups.LineupResponse
 import com.example.footballapi.modelClasses.matchStats.MatchStatsResponse
 import com.example.footballapi.modelClasses.matchSummary.MatchSummary
 import com.example.footballapi.modelClasses.matchTable.matchTableResponse
 import com.example.footballapi.modelClasses.teamMatches.TeamMatchesResponse
 import com.example.footballapi.modelClasses.teamPlayerStats.TeamPlayerStatsResponse
 import com.example.footballapi.modelClasses.youtube_shorts.YouTubeShortsResponseItem
 import okhttp3.ResponseBody
 import retrofit2.Response
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


    @GET("/api/football/team/{match_id}/matches/")
    suspend fun getTeamMatches(
        @Path("match_id") matchId: String
    ): TeamMatchesResponse


    @GET("/api/football/competition/{competition_id}/fixtures/{stage_id}/")
    suspend fun getLeagueMatches(
        @Path("competition_id") competition_id: String,
        @Path("stage_id") stage_id: String
    ): LeagueMatchesResponse



    @GET("/api/football/team/{team_name}/{team_id}/{stage_id}/tables/")
    suspend fun getTeamStandings(
        @Path("team_name") teamNAme: String,
        @Path("team_id") teamId: String,
        @Path("stage_id") stageId: String,
    ): TeamTableResponse


    @GET("/api/football/competition/{compID}/standings/")
    suspend fun getLeagueStandings(
        @Path("compID") compId: String
    ): LeagueStandingsResponse



    @GET("/api/football/team/{team_name}/{team_id}/{stage_id}/playerstats/")
    suspend fun getTeamPlayerStats(
        @Path("team_name") teamNAme: String,
        @Path("team_id") teamId: String,
        @Path("stage_id") stageId: String,
    ): TeamPlayerStatsResponse


    @GET("/api/football/competition/{comp_id}/stats/")
    suspend fun getLeagueTopScorer(
        @Path("comp_id") compeID: String
    ): LeagueTopScorerResponse



    @GET("/api/football/news/latest/{page}/")
    suspend fun getLatestNews(
        @Path("page") pageNo: String
    ): LatestNewsResponse



    @GET("/api/football/news/article/{article_id}/")
    suspend fun getSelectedNewsArticle(
        @Path("article_id") articleId: String
    ): LatestNewsResponse



//    @GET("api/youtube-shorts/")
//    suspend fun getYouTubeShorts(): List<YouTubeShortsResponseItem>


    @GET("api/youtube-shorts/")
    suspend fun getYouTubeShorts(): Response<ResponseBody>




    @GET("/api/football/matches/most-watched")
    suspend fun getMostWatchedHighlights(): GeneralHighlightsResponse

    @GET("/api/football/matches/latest")
    suspend fun getLatestHighlights(): GeneralHighlightsResponse



    @GET("/api/football/matches/team-highlights/{team_name}")
    suspend fun getTeamtHighlights(
        @Path("team_name") teamName : String): TeamHighlightsResponse


}
