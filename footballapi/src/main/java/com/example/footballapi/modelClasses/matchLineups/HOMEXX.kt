package com.example.footballapi.modelClasses.matchLineups

data class HOMEXX(
    val id: String,
    val name: String,
    val playerId: String,
    val playerNameSlug: String,
    val position: Int,
    val score: Score,
    val shortName: String,
    val time: String,
    val type: String
)