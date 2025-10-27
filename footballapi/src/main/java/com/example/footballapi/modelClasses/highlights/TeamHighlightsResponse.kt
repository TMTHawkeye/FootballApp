package com.example.footballapi.modelClasses.highlights

data class TeamHighlightsResponse(
    val matches: List<String>,
    val team: String
)