package com.example.footballapi.modelClasses.TeamTable

data class X(
    val additionalInfo: List<AdditionalInfo>,
    val country: String,
    val countryName: String,
    val hideLeagueTableBadges: Boolean,
    val isLimitedOversMatch: Boolean,
    val isSplit: Boolean,
    val kind: String,
    val name: String,
    val stagePhases: List<StagePhase>,
    val subName: String,
    val tableName: String,
    val teams: List<TeamX>,
    val url: String
)