package com.example.footballapi.modelClasses.matchTable

data class ScoresByPeriod(
    val away: Away,
    val home: Home,
    val label: String,
    val shouldDisplayFirst: Boolean
)