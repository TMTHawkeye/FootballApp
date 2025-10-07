package com.example.footballapp.models.matchmodels

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Parcelable
import android.view.View
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

// Match.kt
@Parcelize
data class Match(
    val id: String,
    val date: String,
    val time: String,
    val team1: Team,
    val team2: Team,
    val team1Goals: Int? = null,
    val team2Goals: Int? = null,
    val competition: String,
    val competitionLogoResId: Int? = null,
    val isExpanded: Boolean = false
) : Parcelable, Serializable {
    val score: String?
        get() = if (team1Goals != null && team2Goals != null) {
            "$team1Goals-$team2Goals"
        } else {
            null
        }

    companion object {
        // Add a serial version UID for Serializable
        private const val serialVersionUID: Long = 1L
    }
}



@Parcelize
data class Team(
    val id: String,
    val name: String,
    val logoResId: Int
) : Parcelable, Serializable {
    companion object {
        private const val serialVersionUID: Long = 1L
    }
}

// MatchDate.kt
data class MatchDate(
    val displayText: String, // Combined text like "24 Aug, Sun"
    val fullDate: String,    // For backend use like "2024-08-24"
    val isSelected: Boolean = false
)

object BlurUtils {

    fun applyBlur(view: View, blurRadius: Float = 25f) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Use RenderEffect for API 31+
            val blurEffect = RenderEffect.createBlurEffect(
                blurRadius, blurRadius, Shader.TileMode.MIRROR
            )
            view.setRenderEffect(blurEffect)
        }
        // For older versions, we'll use a semi-transparent overlay
    }

    fun removeBlur(view: View) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            view.setRenderEffect(null)
        }
    }

    fun captureView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}