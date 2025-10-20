package com.example.footballapi.sealedClasses

sealed class MatchEvent {
    abstract val minute: Int?

    data class GoalEvent(
        override val minute: Int? = null,
        val player: String? = null,
        val team: String? = null,
        val score: String? = null
    ) : MatchEvent()

    data class YellowCardEvent(
        override val minute: Int? = null,
        val player: String? = null,
        val team: String? = null
    ) : MatchEvent()

    data class RedCardEvent(
        override val minute: Int? = null,
        val player: String? = null,
        val team: String? = null
    ) : MatchEvent()

    data class SubstitutionEvent(
        override val minute: Int? = null,
        val playerIn: String? = null,
        val playerOut: String? = null,
        val team: String? = null
    ) : MatchEvent()
}
