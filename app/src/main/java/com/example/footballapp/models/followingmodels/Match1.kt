package com.example.footballapp.models.followingmodels

data class Match1(
    val id: String,
    val homeTeam: String,
    val homeTeamIcon: Int,
    val awayTeam: String,
    val awayTeamIcon: Int,
    val date: String,
    val time: String,
    val status: String = "UPCOMING", // UPCOMING, LIVE, FT
    val score: String? = null,
    val venue: String? = null
)