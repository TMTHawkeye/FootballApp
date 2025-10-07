package com.example.footballapi.modelClasses.allMatches

data class AllMatchesResponseItem(
    val away_score: Int,
    val away_team: AwayTeam,
    val home_score: Int,
    val home_team: HomeTeam,
    val match_id: String,
    val match_status: String,
    val start_datetime: String
)