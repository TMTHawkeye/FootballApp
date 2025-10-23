package com.example.footballapi.leaguePlayerStats

data class Player(
    val Snm: String?=null,
    val athlete_id: String?=null,
    val first_name: String?=null,
    val last_name: String?=null,
    val player_id: String?=null,
    val player_image: String?=null,
    val player_name: String?=null,
    val player_slug: String?=null,
    val rank: Int?=null,
    val scores: Scores?=null,
    val team_badge: String?=null,
    val team_id: String?=null,
    val team_name: String?=null
)