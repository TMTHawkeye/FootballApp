package com.example.footballapi.modelClasses.matchLineups

data class Lineups(
    val awayCoach: List<AwayCoach>,
    val awayStarters: List<AwayStarter>,
    val awaySubs: List<AwaySub>,
    val homeCoach: List<HomeCoach>,
    val homeStarters: List<HomeStarter>,
    val homeSubs: List<HomeSub>
)