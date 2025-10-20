package com.example.footballapi.modelClasses.teamPlayerStats

data class InitialPlayerStats(
    val countryName: String,
    val iconUrl: String,
    val stats: List<Stat>,
    val tabs: List<Tab>
)