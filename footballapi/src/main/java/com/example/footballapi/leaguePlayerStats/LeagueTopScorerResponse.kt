package com.example.footballapi.leaguePlayerStats

data class LeagueTopScorerResponse(
    val competition_country: String,
    val competition_description: String,
    val competition_id: String,
    val competition_name: String,
    val competition_url_name: String,
    val country_name: String,
    val country_name_translated: String,
    val feed: Feed,
    val stats: List<Stat>
)