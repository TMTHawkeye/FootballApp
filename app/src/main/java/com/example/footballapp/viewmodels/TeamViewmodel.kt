package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapi.modelClasses.Matche
 import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.models.Team
import com.example.footballapp.repositories.MatchRepository

class TeamViewmodel(var repo : MatchRepository) : ViewModel() {


    fun setLeague(stage : Stage){
        repo.leagueSelected = stage
    }

    fun getLeague(): Stage? = repo.leagueSelected


    fun setTeam(team : Team){
        repo.teamSelected = team
    }

    fun getTeam(): Team? = repo.teamSelected


    fun setLiveMatches(liveMatches : List<Matche>){
        repo.liveMatches = liveMatches
    }

    fun getLiveMatches(): List<Matche>? = repo.liveMatches

    fun setSelectedNews(news : LatestNewsResponseItem){
        repo.selectedNews = news
    }

    fun getSelectedNews() : LatestNewsResponseItem? = repo.selectedNews
}