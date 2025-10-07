package com.example.footballapi.modelClasses.matchSummary

data class Home(
    val abbr: String,
    val country: String,
    val halftime_score: Int,
    val id: String,
    val name: String,
    val score: Int
)