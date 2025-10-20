package com.example.footballapi.modelClasses.teamPlayerStats

data class TeamPlayerStatsResponse(
    val __N_SSP: Boolean,
    val buildId: String,
    val externalAppConfig: Any,
    val footerLinks: List<FooterLink>,
    val generated: Long,
    val meta: Meta,
    val pageDictFiles: PageDictFiles,
    val pageId: String,
    val pageProps: PageProps,
    val publicDomain: String
)