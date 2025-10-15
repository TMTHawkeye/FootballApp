package com.example.footballapi.modelClasses.matchLineups

data class HomeCoach(
    val id: String,
    val minOut: String,
    val name: String,
    val playerId: String,
    val playerNameSlug: String,
    val position: String,
    val positionId: Int,
    val shortName: String
)