package com.example.footballapi.modelClasses.leagueStandings

data class Stage(
    val badge_url: String,
    val competition_country: String,
    val competition_country_id: String,
    val competition_description: String,
    val competition_history_id: Int,
    val competition_id: String,
    val competition_name: String,
    val competition_url_name: String,
    val country_code: String,
    val country_display_name: String,
    val country_iso_code: String,
    val country_name: String,
    val country_name_translated: String,
    val feed: Feed,
    val league_table: LeagueTable,
    val primary_color: String,
    val score_update: Int,
    val stage_code: String,
    val stage_display_name: String,
    val stage_display_name_long: String,
    val stage_history_id: Int,
    val stage_id: String,
    val stage_name: String
)