package com.example.footballapi.modelClasses.AllCompetitions

data class AllCompetitionsResponse(
    val stages: List<Stage>,
    val suggestions: Suggestions
)