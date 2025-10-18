package com.example.footballapi.modelClasses.selectedNewsArticle

data class SleectedNewsResponse(
    val content: String,
    val links: List<String>,
    val title: String,
    val url: String
)