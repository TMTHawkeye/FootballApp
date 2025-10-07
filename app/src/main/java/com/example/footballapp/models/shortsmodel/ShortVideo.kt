package com.example.footballapp.models.shortsmodel

// Data class for short videos
data class ShortVideo(
    val videoUrl: String,
    val title: String,
    val description: String,
    val likes: Int,
    val isLiked: Boolean = false
)