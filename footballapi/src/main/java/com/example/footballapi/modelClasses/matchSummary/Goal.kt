package com.example.footballapi.modelClasses.matchSummary

data class Goal(
    val minute: Int,
    val player: String,
    val score: String,
    val team: String
)