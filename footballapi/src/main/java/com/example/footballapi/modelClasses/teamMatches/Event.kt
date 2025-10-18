package com.example.footballapi.modelClasses.teamMatches

data class Event(
    val Trp1: String,
    val Trp2: String,
    val away_score: String,
    val away_team: List<AwayTeam>,
    val away_winner_type: Int,
    val event_id: String,
    val event_order: Long,
    val event_order_extra: Long,
    val event_priority: Int,
    val event_start_datetime: Long,
    val event_status: String,
    val event_status_id: Int,
    val event_winner_type: Int,
    val home_score: String,
    val home_team: List<HomeTeam>,
    val media: Media,
    val series_info: SeriesInfo,
    val stage_id: String
)