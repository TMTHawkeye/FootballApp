package com.example.footballapp.models.followingmodels
data class Competition(
    val id: String,
    val name: String,
    var isSelected: Boolean = false
)

data class TopPlayer(
    val name: String,
    val image: Int,
    val team: String,
    val goals: Int
)