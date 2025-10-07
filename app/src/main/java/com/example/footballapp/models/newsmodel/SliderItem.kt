package com.example.footballapp.models.newsmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class SliderItem(
    val imageResId: Int,  // Changed from String to Int for resource ID
    val title: String,
    val date: String
)


@Parcelize
data class NewsItem(
    val imageResId: Int,
    val title: String,
    val description: String,
    val date: String
) : Parcelable