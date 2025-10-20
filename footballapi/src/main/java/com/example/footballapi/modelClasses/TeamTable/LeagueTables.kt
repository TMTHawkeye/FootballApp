package com.example.footballapi.modelClasses.TeamTable

import com.example.footballapi.modelClasses.matchTable.L

data class LeagueTables(
//    val league: League
    val league: Map<String, List<L>>
)