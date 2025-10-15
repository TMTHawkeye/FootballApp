package com.example.footballapi.modelClasses.matchTable

data class Team(
    val draws: Int,
    val form: List<Form>,
    val goalsAgainst: Int,
    val goalsDiff: Int,
    val goalsFor: Int,
    val hasMatchInProgress: Boolean,
    val id: String,
    val losses: Int,
    val lossesOT: Int,
    val name: String,
    val played: Int,
    val points: Int,
    val rank: Int,
    val reg: Int,
    val slug: String,
    val stagePhases: List<StagePhase>,
    val teamBadge: TeamBadge,
    val wins: Int,
    val winsOT: Int
)