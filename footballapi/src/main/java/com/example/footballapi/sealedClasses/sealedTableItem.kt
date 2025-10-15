package com.example.footballapi.sealedClasses

sealed class sealedTableItem {
    data class LeagueDivider(val leagueName: String) : sealedTableItem()
    object HeaderRow : sealedTableItem()
    data class TeamRow(
        val position: Int,
        val teamName: String,
        val teamLogo: String,
        val matchesPlayed: Int,
        val wins: Int,
        val draws: Int,
        val losses: Int,
        val goalDifference: Int,
        val points: Int,
        val isCurrentTeam1: Boolean = false,
        val isCurrentTeam2: Boolean = false
    ) : sealedTableItem()
}
