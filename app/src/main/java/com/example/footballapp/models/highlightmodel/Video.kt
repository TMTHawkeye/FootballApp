package com.example.footballapp.models.highlightmodel





import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val title: String,
    val duration: String,
    val thumbnailRes: Int,
    val videoUrl: String
) : Parcelable