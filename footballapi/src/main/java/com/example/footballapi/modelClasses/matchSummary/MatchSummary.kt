package com.example.footballapi.modelClasses.matchSummary

data class MatchSummary(
    val competition: Competition,
    val events: Events,
    val kickoff: String,
    val match_id: String,
    val media: List<Media>,
    val scores: Scores,
    val status: String,
    val teams: Teams
)