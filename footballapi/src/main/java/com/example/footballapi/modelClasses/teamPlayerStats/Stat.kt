package com.example.footballapi.modelClasses.teamPlayerStats

data class Stat(
    val players: List<Player>,
    val statId: String,
    val statName: String,
    val statSlug: String
)