package com.example.footballapp.utils

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class DepthPageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.apply {
            when {
                position < -1 -> alpha = 0f
                position <= 0 -> { // moving left
                    alpha = 1f
                    translationX = 0f
                    scaleX = 1f
                    scaleY = 1f
                }
                position <= 1 -> { // moving right
                    alpha = 1 - position
                    translationX = page.width * -position
                    val scale = 0.85f + (1 - abs(position)) * 0.15f
                    scaleX = scale
                    scaleY = scale
                }
                else -> alpha = 0f
            }
        }
    }
}
