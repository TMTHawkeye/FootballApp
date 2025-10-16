package com.example.footballapp.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FollowRepository(context: Context) {

    companion object {
        private const val PREF_NAME = "follow_prefs"
        private const val FOLLOWED_LEAGUES_KEY = "followed_leagues"
    }

    private val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    // Get all followed league IDs
    suspend fun getFollowedLeagues(): List<String> = withContext(Dispatchers.IO) {
        val joined = prefs.getString(FOLLOWED_LEAGUES_KEY, "") ?: ""
        if (joined.isEmpty()) emptyList() else joined.split(",")
    }

    // Add a new league ID
    suspend fun followLeague(leagueId: String) = withContext(Dispatchers.IO) {
        val currentList = getFollowedLeagues().toMutableList()
        if (!currentList.contains(leagueId)) {
            currentList.add(leagueId)
            saveList(currentList)
        }
    }

    // Remove league ID (unfollow)
    suspend fun unfollowLeague(leagueId: String) = withContext(Dispatchers.IO) {
        val currentList = getFollowedLeagues().toMutableList()
        currentList.remove(leagueId)
        saveList(currentList)
    }

    // Check if a league is followed
    suspend fun isLeagueFollowed(leagueId: String): Boolean = withContext(Dispatchers.IO) {
        getFollowedLeagues().contains(leagueId)
    }

    // Save list as comma-separated string
    private fun saveList(list: List<String>) {
        val joined = list.joinToString(",")
        prefs.edit().putString(FOLLOWED_LEAGUES_KEY, joined).apply()
    }
}
