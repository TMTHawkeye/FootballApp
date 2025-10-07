package com.example.footballapi.modelClasses.matchSummary

data class Competition(
    val country: String,
    val description: String,
    val id: String,
    val name: String,
    val stage_id: String,
    val stage_name: String
)