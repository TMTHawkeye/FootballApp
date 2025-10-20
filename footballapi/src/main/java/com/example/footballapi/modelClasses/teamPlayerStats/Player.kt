package com.example.footballapi.modelClasses.teamPlayerStats

data class Player(
    val name: String,
    val playerId: String,
    val playerKey: String,
    val playerNameSlug: String,
    val rank: Int,
    val statAmount: String,
    val teamBadge: TeamBadge,
    val teamId: String,
    val teamName: String
)