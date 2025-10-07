package com.example.footballapi.modelClasses.matchStats

data class MatchStatsResponse(
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