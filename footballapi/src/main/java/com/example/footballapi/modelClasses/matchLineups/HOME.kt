package com.example.footballapi.modelClasses.matchLineups

data class HOME(
    val id: String,
    val name: String,
    val playerId: String,
    val playerNameSlug: String,
    val position: Int,
    val shortName: String,
    val time: String,
    val type: String
)