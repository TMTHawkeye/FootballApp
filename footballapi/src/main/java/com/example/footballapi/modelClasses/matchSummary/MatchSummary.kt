package com.example.footballapi.modelClasses.matchSummary

data class MatchSummary(
    val competition: Competition?=null,
    val events: Events?=null,
    val kickoff: String?=null,
    val match_id: String?=null,
    val media: List<Media>?=null,
    val scores: Scores?=null,
    val status: String?=null,
    val teams: Teams?=null
)