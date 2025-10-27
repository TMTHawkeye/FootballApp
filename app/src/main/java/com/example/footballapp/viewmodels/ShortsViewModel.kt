package com.example.footballapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.footballapp.repositories.FollowRepository
import kotlinx.coroutines.launch

class ShortsViewModel(private val repo: FollowRepository) : ViewModel() {

    val likedShorts = repo.likedShortsFlow

    fun toggleLike(videoUrl: String) {
        viewModelScope.launch {
            if (repo.isLiked(videoUrl)) repo.unlikeShort(videoUrl)
            else repo.likeShort(videoUrl)
        }
    }
}
