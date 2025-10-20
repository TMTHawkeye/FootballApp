package com.example.footballapi.modelClasses.TeamTable

data class PageProps(
    val activeSport: String,
    val adPaths: AdPaths,
    val adsTargeting: AdsTargeting,
    val dropDownItemRootLink: String,
    val initialData: InitialData,
    val layoutContext: LayoutContext,
    val leagueTableTab: String,
    val segmentTracking: SegmentTracking,
    val stageId: String,
    val teamId: String,
    val teamTabs: List<TeamTab>
)