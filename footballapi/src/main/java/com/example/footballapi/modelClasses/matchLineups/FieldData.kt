package com.example.footballapi.modelClasses.matchLineups

data class FieldData(
    val awayFormation: String,
    val awayTeamName: String,
    val canRenderField: Boolean,
    val homeFormation: String,
    val homeTeamName: String
)