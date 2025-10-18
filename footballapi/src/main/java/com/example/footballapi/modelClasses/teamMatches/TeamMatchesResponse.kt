package com.example.footballapi.modelClasses.teamMatches

data class TeamMatchesResponse(
    val NewsTag: String,
    val abbreviation: String,
    val country_id: String,
    val country_name: String,
    val feed: Feed,
    val has_squad: Boolean,
    val has_video: Boolean,
    val is_national_team: Boolean,
    val primary_color: String,
    val provider_ids: ProviderIds,
    val secondary_color: String,
    val stages: List<Stage>,
    val stat_player_stage_ids: List<String>,
    val stat_stage_ids: List<String>,
    val stat_team_stage_ids: List<String>,
    val team_badge: String,
    val team_id: String,
    val team_name: String,
    val to_be_decided: Int,
    val total_objects: Int
)