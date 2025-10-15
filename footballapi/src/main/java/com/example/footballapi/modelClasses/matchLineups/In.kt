package com.example.footballapi.modelClasses.matchLineups

data class In(
    val id: String,
    val name: String,
    val playerId: String,
    val playerNameSlug: String,
    val shortName: String,
    val subPlayerId: String,
    val subTime: String,
    val subType: String,
    val team: String,
    val time: Int
)