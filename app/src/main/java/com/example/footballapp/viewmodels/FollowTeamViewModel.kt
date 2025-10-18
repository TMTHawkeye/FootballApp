package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
 import com.example.footballapp.repositories.FollowTeamRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowTeamViewModel(private val repository: FollowTeamRepository) : ViewModel()  {

    private val _followedTeams = MutableStateFlow<List<String>>(emptyList())
    val followedTeams: StateFlow<List<String>> = _followedTeams

    init {
        loadFollowedTeams()
    }

    private fun loadFollowedTeams() {
        viewModelScope.launch {
            _followedTeams.value = repository.getFollowedLeagues()
        }
    }

    fun followTeam(leagueId: String) {
        viewModelScope.launch {
            repository.followLeague(leagueId)
            loadFollowedTeams()
        }
    }

    fun unfollowTeam(leagueId: String) {
        viewModelScope.launch {
            repository.unfollowLeague(leagueId)
            loadFollowedTeams()
        }
    }

    fun isTeamFollowed(leagueId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(repository.isLeagueFollowed(leagueId))
        }
    }

    fun toggleFollowTeam(leagueId: String) {
        viewModelScope.launch {
            if (repository.isLeagueFollowed(leagueId)) {
                repository.unfollowLeague(leagueId)
            } else {
                repository.followLeague(leagueId)
            }
            loadFollowedTeams() // Refresh state
        }
    }


}