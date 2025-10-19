package com.example.footballapp.repositories

import android.content.Context
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.models.Team

class MatchRepository (var context : Context){

    var leagueSelected : Stage? = null
    var matchSelected : Matche? = null
    var teamSelected : Team? = null
    var selectedNews : LatestNewsResponseItem? = null
    var liveMatches : List<Matche>? = null

}