package com.example.footballapi.modelClasses.teamPlayerStats

data class PageProps(
    val activeSport: String,
    val adPaths: AdPaths,
    val adsTargeting: AdsTargeting,
    val initialPlayerStats: InitialPlayerStats,
    val initialTeamDetails: InitialTeamDetails,
    val layoutContext: LayoutContext,
    val rootLink: String,
    val segmentTracking: SegmentTracking,
    val stageId: String,
    val teamId: String,
    val teamTabs: List<TeamTab>
)