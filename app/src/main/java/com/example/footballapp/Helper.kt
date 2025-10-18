package com.example.footballapp

import android.view.View
import com.example.footballapi.modelClasses.matchSummary.Events
import com.example.footballapi.sealedClasses.MatchEvent

object Helper {

    var ApiResultTAG = "ApiResult_Tag"
    var MATCH_ID = "MATCH_ID"
    var LEAGUE_ID = "LEAGUE_ID"
    var TEAM_ID = "TEAM_ID"
    var TEAM_NAME = "TEAM_NAME"
    var imagePrefixCompetition = "https://storage.livescore.com/images/competition/high/"
    var imagePrefix = "https://storage.livescore.com/images/team/high/"

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.invisible() {
        this.visibility = View.INVISIBLE
    }


    fun Events.toMatchEventList(): List<MatchEvent> {
        val allEvents = mutableListOf<MatchEvent>()

        goals?.forEach {
            allEvents.add(MatchEvent.GoalEvent(it.minute, it.player, it.team, it.score))
        }

        yellow_cards?.forEach {
            allEvents.add(MatchEvent.YellowCardEvent(it.minute, it.player, it.team))
        }

        red_cards?.forEach {
            allEvents.add(MatchEvent.RedCardEvent(it.minute, it.player, it.team))
        }

        substitutions?.forEach {
            allEvents.add(MatchEvent.SubstitutionEvent(it.minute, it.playerIn, it.playerOut, it.team))
        }

        // Sort events by minute (ascending)
        return allEvents.sortedBy { it.minute }
    }


      fun formatMatchStatus(status: String?): String {
        if (status.isNullOrBlank()) return ""

        return when {
            // Handle seconds (double apostrophe)
            status.endsWith("''") -> {
                status.removeSuffix("''") + " sec"
            }

            // Handle minutes (single apostrophe)
            status.endsWith("'") -> {
                status.removeSuffix("'") + " min"
            }

            // Keep normal statuses (HT, FT, NS, etc.)
            else -> status
        }
    }

}