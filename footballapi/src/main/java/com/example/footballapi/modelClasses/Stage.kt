package com.example.footballapi.modelClasses

data class Stage(
    val Games: Games? = null,
    val badge_url: String? = null,
    val competition_description: String? = null,
    val competition_id: String? = null,
    val competition_name: String? = null,
    val competition_stage: String? = null,
    val competition_url_name: String? = null,
    val country_code: String? = null,
    val country_name: String? = null,
    val country_name_translated: String? = null,
    val country_short_name: String? = null,
    val feed: Feed? = null,
    val matches: List<Matche>? = null,
    val primary_color: String? = null,
    val score_update: Int? = null,
    val secondColor: String? = null,
    val stage_code: String? = null,
    val stage_id: String? = null,
    val stage_name: String? = null
)
