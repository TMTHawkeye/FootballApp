package com.example.footballapi.modelClasses.latestNews

data class LatestNewsResponseItem(
    val id: String,
    val image: String,
    val link: String,
    val preview: String,
    val publish_time: String,
    val publish_timestamp: String,
    val publisher: String,
    val publisher_logo: String,
    val title: String
)