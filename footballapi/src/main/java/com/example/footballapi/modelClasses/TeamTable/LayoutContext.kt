package com.example.footballapi.modelClasses.TeamTable

data class LayoutContext(
    val activeHeaderId: String,
    val isHorizontalNavigationVisible: Boolean,
    val leftMenuLinks: LeftMenuLinks,
    val metaParams: MetaParams
)