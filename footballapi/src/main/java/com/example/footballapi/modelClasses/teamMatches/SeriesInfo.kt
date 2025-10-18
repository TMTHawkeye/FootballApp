package com.example.footballapi.modelClasses.teamMatches

data class SeriesInfo(
    val aggregate_score_away: Int,
    val aggregate_score_home: Int,
    val current_leg: Int,
    val total_legs: Int
)