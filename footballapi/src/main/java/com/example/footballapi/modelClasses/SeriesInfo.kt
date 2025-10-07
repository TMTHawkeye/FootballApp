package com.example.footballapi.modelClasses

data class SeriesInfo(
    val aggScoreTeam1: Int,
    val aggScoreTeam2: Int,
    val currentLeg: Int,
    val totalLegs: Int
)