package com.example.footballapi.modelClasses.matchStats

data class ScoresByPeriod(
    val away: Away,
    val home: Home,
    val label: String,
    val shouldDisplayFirst: Boolean
)