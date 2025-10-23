package com.example.footballapp.models.shortsmodel

// Data class for short videos
data class ShortVideo(
    val videoUrl: String?=null,
    val title: String?=null,
    val description: String?=null,
    val likes: Int?=null,
    val isLiked: Boolean = false
)