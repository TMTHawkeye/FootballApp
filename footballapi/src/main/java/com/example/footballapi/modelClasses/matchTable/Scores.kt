package com.example.footballapi.modelClasses.matchTable

data class Scores(
    val aggregateAwayScore: String,
    val aggregateHomeScore: String,
    val awayOvertimeScore: String,
    val awayTeamName: String,
    val awayTeamScore: String,
    val awayTeamSlug: String,
    val eventId: String,
    val homeOvertimeScore: String,
    val homeTeamName: String,
    val homeTeamScore: String,
    val homeTeamSlug: String,
    val matchStatusDetails: MatchStatusDetails,
    val penaltyAwayScore: Int,
    val penaltyHomeScore: Int,
    val scoresByPeriod: List<ScoresByPeriod>,
    val statusInt: Int
)