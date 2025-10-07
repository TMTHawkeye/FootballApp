package com.example.footballapi.sealedClasses

sealed class MatchEvent {
    abstract val minute: Int

    data class GoalEvent(
        override val minute: Int,
        val player: String,
        val team: String,
        val score: String
    ) : MatchEvent()

    data class YellowCardEvent(
        override val minute: Int,
        val player: String,
        val team: String
    ) : MatchEvent()

    data class RedCardEvent(
        override val minute: Int,
        val player: String,
        val team: String
    ) : MatchEvent()

    data class SubstitutionEvent(
        override val minute: Int,
        val playerIn: String,
        val playerOut: String,
        val team: String
    ) : MatchEvent()
}
