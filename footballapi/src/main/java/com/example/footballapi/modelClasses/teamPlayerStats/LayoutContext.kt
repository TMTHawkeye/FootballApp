package com.example.footballapi.modelClasses.teamPlayerStats

data class LayoutContext(
    val activeHeaderId: String,
    val isFeaturedArticlesShown: Boolean,
    val isHorizontalNavigationVisible: Boolean,
    val leftMenuLinks: LeftMenuLinks,
    val metaParams: MetaParams,
    val page: String
)