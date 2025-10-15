package com.example.footballapi.modelClasses.matchStats

data class Statistics(
    val corners: List<Int>,
    val counterAttacks: List<Int>,
    val crosses: List<Int>,
    val fouls: List<Int>,
    val goalKicks: List<Int>,
    val goalkeeperSaves: List<Int>,
    val offsides: List<Int>,
    val possession: List<Int>,
    val redCards: List<Int>,
    val shotsBlocked: List<Int>,
    val shotsOffTarget: List<Int>,
    val shotsOnTarget: List<Int>,
    val throwIns: List<Int>,
    val treatments: List<Any>,
    val yellowCards: List<Int>,
    val yellowRedCards: List<Int>
)