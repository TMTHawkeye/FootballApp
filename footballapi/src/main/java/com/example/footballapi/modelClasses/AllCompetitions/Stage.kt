package com.example.footballapi.modelClasses.AllCompetitions

data class Stage(
    val badge_url: String,
    val competition_code: String,
    val competition_desc: String,
    val competition_id: String,
    val competition_name: String,
    val competition_stage_type: String,
    val competition_url_name: String,
    val country_code: String,
    val country_name: String,
    val country_slug: String,
    val first_color: String,
    val sport_id: Int,
    val stage_code: String,
    val stage_id: String,
    val stage_name: String,
    val stage_short: String
)