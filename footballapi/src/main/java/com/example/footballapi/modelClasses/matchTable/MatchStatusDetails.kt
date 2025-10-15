package com.example.footballapi.modelClasses.matchTable

data class MatchStatusDetails(
    val isFinished: Boolean,
    val isInProgress: Boolean,
    val isNotStarted: Boolean,
    val liveTime: String,
    val status: String
)