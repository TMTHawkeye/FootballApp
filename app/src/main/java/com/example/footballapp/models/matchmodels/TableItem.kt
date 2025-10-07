package com.example.footballapp.models.matchmodels

import com.example.footballapp.R

// In TableFragment.kt
data class TableItem(
    val position: Int,
    val teamName: String,
    val matchesPlayed: Int,
    val wins: Int,
    val draws: Int,
    val losses: Int,
    val goalDifference: Int,
    val points: Int,
    val teamLogo: Int = R.drawable.app_icon, // Default logo
    val isCurrentTeam1: Boolean = false,
    val isCurrentTeam2: Boolean = false
)