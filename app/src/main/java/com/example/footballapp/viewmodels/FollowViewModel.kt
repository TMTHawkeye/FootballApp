//package com.example.footballapp.viewmodels
//
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.footballapp.repositories.FollowRepository
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.launch
//
//class FollowViewModel(private val repository: FollowRepository) : ViewModel() {
//
//    private val _followedLeagues = MutableStateFlow<List<String>>(emptyList())
//    val followedLeagues: StateFlow<List<String>> = _followedLeagues
//
//    init {
//        loadFollowedLeagues()
//    }
//
//    private fun loadFollowedLeagues() {
//        viewModelScope.launch {
//            _followedLeagues.value = repository.getFollowedLeagues()
//        }
//    }
//
//    fun followLeague(leagueId: String) {
//        viewModelScope.launch {
//            repository.followLeague(leagueId)
//            loadFollowedLeagues()
//        }
//    }
//
//    fun unfollowLeague(leagueId: String) {
//        viewModelScope.launch {
//            repository.unfollowLeague(leagueId)
//            loadFollowedLeagues()
//        }
//    }
//
//    fun isLeagueFollowed(leagueId: String, callback: (Boolean) -> Unit) {
//        viewModelScope.launch {
//            callback(repository.isLeagueFollowed(leagueId))
//        }
//    }
//
//    fun toggleFollowLeague(leagueId: String) {
//        viewModelScope.launch {
//            if (repository.isLeagueFollowed(leagueId)) {
//                repository.unfollowLeague(leagueId)
//            } else {
//                repository.followLeague(leagueId)
//            }
//            loadFollowedLeagues() // Refresh state
//        }
//    }
//
//
//
//
//
//
//
//
//
//}



package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.models.FollowedLeague
import com.example.footballapp.repositories.FollowRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FollowViewModel(private val repository: FollowRepository) : ViewModel() {

    private val _followedLeagues = MutableStateFlow<List<FollowedLeague>>(emptyList())
    val followedLeagues: StateFlow<List<FollowedLeague>> = _followedLeagues

    init {
        loadFollowedLeagues()
    }

    private fun loadFollowedLeagues() {
        viewModelScope.launch {
            _followedLeagues.value = repository.getFollowedLeagues()
        }
    }

    fun followLeague(leagueId: String, leagueName: String) {
        viewModelScope.launch {
            repository.followLeague(leagueId, leagueName)
            loadFollowedLeagues()
        }
    }

    fun unfollowLeague(leagueId: String) {
        viewModelScope.launch {
            repository.unfollowLeague(leagueId)
            loadFollowedLeagues()
        }
    }

    fun toggleFollowLeague(leagueId: String, leagueName: String) {
        viewModelScope.launch {
            if (repository.isLeagueFollowed(leagueId)) {
                repository.unfollowLeague(leagueId)
            } else {
                repository.followLeague(leagueId, leagueName)
            }
            loadFollowedLeagues()
        }
    }

    fun isLeagueFollowed(leagueId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(repository.isLeagueFollowed(leagueId))
        }
    }
}
