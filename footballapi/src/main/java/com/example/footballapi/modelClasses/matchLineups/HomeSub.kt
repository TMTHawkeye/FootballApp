package com.example.footballapi.modelClasses.matchLineups

data class HomeSub(
    val id: String,
    val minOut: String,
    val name: String,
    val number: Int,
    val playerId: String,
    val playerNameSlug: String,
    val position: String,
    val positionId: Int,
    val shortName: String,
    val sub: Sub
)