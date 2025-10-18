package com.example.footballapi.modelClasses.teamMatches

data class Stage(
    val CompCnmt: String,
    val badge_url: String,
    val category_code: String,
    val category_display_name: String,
    val category_id: String,
    val category_name: String,
    val category_name_translated: String,
    val competition_country: String,
    val competition_description: String,
    val competition_history_id: Int,
    val competition_id: String,
    val competition_name: String,
    val competition_url_name: String,
    val cox: Int,
    val events: List<Event>,
    val primary_color: String,
    val score_update: Int,
    val sport_id: Int,
    val stage_code: String,
    val stage_display_name: String,
    val stage_history_id: Int,
    val stage_id: String,
    val stage_ids: StageIds,
    val stage_name: String,
    val tid: String
)