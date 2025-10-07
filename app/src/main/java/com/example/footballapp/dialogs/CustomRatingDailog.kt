package com.example.footballapp.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.footballapp.R
import com.example.footballapp.databinding.CustomRatingDailogBinding
import com.example.footballapp.models.matchmodels.BlurUtils
import com.example.footballapp.utils.feedBackMyFootballEmail
import com.example.footballapp.utils.ratingMyFootballApp

class CustomRatingDailog(private val activity: Activity) : Dialog(activity) {
    var _binding: CustomRatingDailogBinding? = null
    val binding get() = _binding!!

    private lateinit var blurOverlay: ImageView
    private var blurredBackground: Bitmap? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        _binding = CustomRatingDailogBinding.inflate(LayoutInflater.from(context))

        binding.linsesProvided1.setOnClickListener {
            dismiss()
        }

        rootLayout.addView(blurOverlay)
        rootLayout.addView(binding.root)

        setContentView(rootLayout)

        // Convert 8sdp to pixels for programmatic margin setting
        val margin8sdp = convertSdpToPixels(8f)

        // Set dialog window properties for bottom positioning with margins
        window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            // Position at bottom center
            setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL)

            // Set bottom margin using window attributes
            val windowParams = WindowManager.LayoutParams().apply {
                copyFrom(attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                // Set bottom margin
                verticalMargin = 0f
            }
            attributes = windowParams
        }

        setCancelable(true)

        binding.rateBar.setOnRatingBarChangeListener { _, rateus, _ ->
            val imageResId = when {
                rateus >= 5.0 -> R.drawable.mood5
                rateus >= 4.0 -> R.drawable.mood4
                rateus >= 3.0 -> R.drawable.mood3
                rateus >= 2.0 -> R.drawable.mood2
                rateus >= 1.0 -> R.drawable.mood1
                rateus >= 0.0 -> R.drawable.mood1
                else -> R.drawable.mood1
            }

            binding.imagchange.setImageResource(imageResId)
            updateCertificateRating(rateus)
        }

        binding.submitBtn.setOnClickListener {
            when (binding.rateBar.rating) {
                0.0f -> {
                    Toast.makeText(context, R.string.kindly_rate, Toast.LENGTH_SHORT).show()
                    resetCertificateUpdated()
                }
                1.0f -> {
                    activity.feedBackMyFootballEmail()
                    dismiss()
                    resetCertificateUpdated()
                }
                2.0f -> {
                    activity.feedBackMyFootballEmail()
                    dismiss()
                    resetCertificateUpdated()
                }
                3.0f -> {
                    activity.feedBackMyFootballEmail()
                    dismiss()
                    resetCertificateUpdated()
                }
                4.0f -> {
                    resetCertificateUpdated()
                    activity.feedBackMyFootballEmail()
                    dismiss()
                }
                5.0f -> {
                    resetCertificateUpdated()
                    activity.ratingMyFootballApp()
                    dismiss()
                }
                else -> {
                    activity.feedBackMyFootballEmail()
                    dismiss()
                    resetCertificateUpdated()
                }
            }
        }
    }

    private fun convertSdpToPixels(sdpValue: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sdpValue,
            context.resources.displayMetrics
        ).toInt()
    }

    override fun onStart() {
        super.onStart()
        // Apply blur effect when the dialog starts
        applyBlurToBackground()

        // Set margins after dialog is shown to ensure proper positioning
        window?.apply {
            val params = attributes
            // Set bottom margin programmatically
            params.y = convertSdpToPixels(8f) // 8sdp bottom margin
            attributes = params
        }
    }

    override fun onStop() {
        super.onStop()
        // Remove blur effect when the dialog stops
        removeBlurFromBackground()
    }

    override fun show() {
        super.show()
        // Note: Blur is now applied in onStart() which is called after onCreate()
    }

    override fun dismiss() {
        super.dismiss()
        removeBlurFromBackground()
        // Clean up
        blurredBackground?.recycle()
        blurredBackground = null
    }

    private fun applyBlurToBackground() {
        // Get the activity's root view (behind the dialog)
        val activityRootView = activity.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)

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
        val activityRootView = activity.window?.decorView?.findViewById<ViewGroup>(android.R.id.content)

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

    private fun updateCertificateRating(rateus: Float) {
        when {
            rateus >= 5.0 -> {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.imagchange2.visibility = View.INVISIBLE
            }
            rateus >= 4.0 -> {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.imagchange2.visibility = View.INVISIBLE
            }
            rateus >= 3.0 -> {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.imagchange2.visibility = View.INVISIBLE
            }
            rateus >= 2.0 -> {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.imagchange2.visibility = View.INVISIBLE
            }
            rateus >= 1.0 -> {
                binding.constraintLayout.visibility = View.VISIBLE
                binding.imagchange2.visibility = View.INVISIBLE
            }
            rateus >= 0.0 -> {
                binding.constraintLayout.visibility = View.INVISIBLE
                binding.imagchange2.visibility = View.VISIBLE
            }
            else -> {
                // Handle other cases if needed
            }
        }
    }

    private fun resetCertificateUpdated() {
        binding.imagchange.setImageResource(R.drawable.mood1)
        binding.submitBtn.visibility = View.VISIBLE
        binding.linedot.visibility = View.VISIBLE
        binding.imagchange2.visibility = View.VISIBLE
        binding.constraintLayout.visibility = View.INVISIBLE
        binding.tabtext.visibility = View.GONE
        binding.rateBar.rating = 0f
    }
}