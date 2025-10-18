package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import com.example.footballapi.modelClasses.Matche
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.models.Team
import com.example.footballapp.repositories.MatchRepository

class TeamViewmodel(var repo : MatchRepository) : ViewModel() {


    fun setTeam(team : Team){
        repo.teamSelected = team
    }

    fun getTeam(): Team? = repo.teamSelected

    fun setSelectedNews(news : LatestNewsResponseItem){
        repo.selectedNews = news
    }

    fun getSelectedNews() : LatestNewsResponseItem? = repo.selectedNews
}