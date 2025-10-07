package com.example.footballapi.modelClasses.matchStats

data class PageDictFiles(
    val lsBetVideo: LsBetVideo,
    val matchDetail: MatchDetail,
    val matchScore: MatchScore,
    val matchTabs: MatchTabs,
    val news: News,
    val statistics: Statistics,
    val streaming: Streaming
)