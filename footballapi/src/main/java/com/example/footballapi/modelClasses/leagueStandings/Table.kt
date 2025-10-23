package com.example.footballapi.modelClasses.leagueStandings

data class Table(
    val phr_x: List<PhrX>,
    val table_type: Int,
    val team: List<TeamX>
)