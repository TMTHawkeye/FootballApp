package com.example.footballapi.modelClasses.leagueMatches

data class Fixture(
    val away_score: Any,
    val away_team: String,
    val competition_id: String,
    val competition_name: String,
    val country: String,
    val date: Long,
    val home_score: Any,
    val home_team: String,
    val match_id: String,
    val stage_id: String,
    val stage_name: String,
    val status: String
)