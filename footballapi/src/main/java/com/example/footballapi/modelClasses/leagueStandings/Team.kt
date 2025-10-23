package com.example.footballapi.modelClasses.leagueStandings

data class Team(
    val form: List<Form>,
    val goal_difference: Int,
    val points: Int,
    val rank: Int,
    val team_badge: String,
    val team_id: String,
    val team_name: String
)