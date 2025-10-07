package com.example.footballapp.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.footballapp.databinding.DialogLoadingBinding
import com.example.footballapp.models.matchmodels.BlurUtils

class AdsLoadingDialog(context: Context) : Dialog(context) {

    private lateinit var binding: DialogLoadingBinding
    private lateinit var blurOverlay: ImageView
    private var blurredBackground: Bitmap? = null
    private var originalBackgroundView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLoadingBinding.inflate(layoutInflater)

        // Create a container with blur background
        val rootLayout = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        // Create overlay for blur effect
        blurOverlay = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(Color.argb(150, 0, 0, 0)) // Dark semi-transparent overlay
        }

        rootLayout.addView(blurOverlay)
        rootLayout.addView(binding.root)

        setContentView(rootLayout)

        // Set dialog properties
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setCancelable(false)
    }

    override fun show() {
        // Apply blur effect to the underlying activity (behind the dialog)
        applyBlurToBackground()
        super.show()
    }

    override fun dismiss() {
        super.dismiss()
        removeBlurFromBackground()
        // Clean up
        blurredBackground?.recycle()
        blurredBackground = null
    }

    fun setMessage(message: String) {
        binding.textViewMessage.text = message
    }

    private fun applyBlurToBackground() {
        // Get the activity's root view (behind the dialog)
        val activity = context as? android.app.Activity
        val activityRootView = activity?.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)

        if (activityRootView != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Use RenderEffect for real blur on API 31+ (only on the activity background)
                BlurUtils.applyBlur(activityRootView, 25f)
            } else {
                // For older versions, use a captured and manually blurred background
                try {
                    blurredBackground = createBlurredBackground(activityRootView)
                    blurOverlay.setImageBitmap(blurredBackground)
                } catch (e: Exception) {
                    // Fallback to dark overlay
                    blurOverlay.setBackgroundColor(Color.argb(180, 0, 0, 0))
                }
            }
        }
    }

    private fun removeBlurFromBackground() {
        // Get the activity's root view (behind the dialog)
        val activity = context as? android.app.Activity
        val activityRootView = activity?.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)

        if (activityRootView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            BlurUtils.removeBlur(activityRootView)
        }
    }

    private fun createBlurredBackground(view: View): Bitmap {
        // Capture the screen behind the dialog
        val bitmap = BlurUtils.captureView(view)

        // Apply a simple blur effect
        return applySimpleBlur(bitmap)
    }

    private fun applySimpleBlur(bitmap: Bitmap): Bitmap {
        // Create a blurred version with dark overlay
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(result)

        // Draw the original bitmap
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Apply a dark overlay to simulate blur
        canvas.drawColor(Color.argb(120, 0, 0, 0))

        bitmap.recycle()
        return result
    }
}