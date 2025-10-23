package com.example.footballapi.modelClasses.matchLineups

data class HomeStarter(
    val card: String?=null,
    val fieldPosition: String?=null,
    val goal: Goal?=null,
    val id: String?=null,
    val minOut: String?=null,
    val name: String?=null,
    val number: Int?=null,
    val playerId: String?=null,
    val playerNameSlug: String?=null,
    val position: String?=null,
    val positionId: Int?=null,
    val shortName: String?=null,
    val sub: Sub?=null
)