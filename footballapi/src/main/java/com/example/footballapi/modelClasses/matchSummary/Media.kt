package com.example.footballapi.modelClasses.matchSummary

data class Media(
    val allowed_countries: List<String>,
    val event_id: String,
    val provider: String,
    val type: String
)