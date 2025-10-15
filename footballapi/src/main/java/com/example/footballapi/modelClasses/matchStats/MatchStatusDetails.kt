package com.example.footballapi.modelClasses.matchStats

data class MatchStatusDetails(
    val isFinished: Boolean,
    val isInProgress: Boolean,
    val isNotStarted: Boolean,
    val liveTime: String,
    val status: String
)