package com.example.footballapp.activities.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivitySettingBinding
import com.example.footballapp.dialogs.CustomRatingDailog

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySettingBinding.inflate(layoutInflater)
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


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.testSounddLt.setOnClickListener {
            startActivity(Intent(this@SettingActivity, LanguageActivity::class.java)
                .putExtra("intentFrom", "fromSettings"))
        }


        val customRatingDialog =
            CustomRatingDailog(this)



        binding.rateusLtLt.setOnClickListener {

            customRatingDialog.show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}