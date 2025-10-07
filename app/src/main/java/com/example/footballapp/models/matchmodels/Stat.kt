package com.example.footballapp.models.matchmodels

// In StatsFragment.kt
data class Stat(
    val name: String,
    val team1Value: Int,
    val team2Value: Int,
    val isPercentage: Boolean = false
) {
    // Helper function to format values
    fun getFormattedValue(value: Int): String {
        return if (isPercentage) "$value%" else value.toString()
    }
}