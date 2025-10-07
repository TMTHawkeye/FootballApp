package com.example.footballapi.modelClasses.matchSummary

import com.example.footballapi.sealedClasses.MatchEvent

data class Events(
    val goals: List<Goal>,
    val red_cards: List<MatchEvent.RedCardEvent>,
    val substitutions: List<MatchEvent.SubstitutionEvent>,
    val yellow_cards: List<MatchEvent.YellowCardEvent>
)