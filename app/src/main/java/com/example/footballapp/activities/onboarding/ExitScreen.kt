package com.example.footballapp.activities.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityExitScreenBinding

import com.example.footballapp.utils.AdsLoadingDialog
import com.example.footballapp.utils.TryAgainDialog
import kotlin.system.exitProcess

class ExitScreen : BaseActivity() {
    private lateinit var binding: ActivityExitScreenBinding
    private lateinit var loadingDialog: TryAgainDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityExitScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true // For dark icons (use with light backgrounds)
            // OR
            isAppearanceLightStatusBars = false // For light icons (use with dark backgrounds)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadingDialog = TryAgainDialog(this)

        binding.autoCleanerr.setOnClickListener {
            finish()
        }

        binding.leaveLt.setOnClickListener {
            // Show the dialog when button is clicked
            loadingDialog.show()

            // Simulate ad loading for 3 seconds before exiting
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialog.dismiss()
                finishAffinity()
                exitProcess(0)
            }, 3000)
        }

        binding.followingLt.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("fragment_position", 1)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.higlightsLt.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("fragment_position", 3)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


    }
}