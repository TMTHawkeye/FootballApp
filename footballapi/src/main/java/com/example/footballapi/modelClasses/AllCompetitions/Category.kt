package com.example.footballapi.modelClasses.AllCompetitions

data class Category(
    val category_code: String,
    val category_id: String,
    val category_name: String,
    val category_slug: String,
    val sport_id: Int
)