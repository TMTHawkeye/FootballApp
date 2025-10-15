package com.example.footballapi.modelClasses.matchTable

data class L(
    val additionalInfo: List<Any?>,
    val country: String,
    val countryName: String,
    val hideLeagueTableBadges: Boolean,
    val isLimitedOversMatch: Any,
    val isSplit: Boolean,
    val kind: String,
    val name: String,
    val stagePhases: List<StagePhase>,
    val subName: String,
    val teams: List<Team>,
    val url: String
)