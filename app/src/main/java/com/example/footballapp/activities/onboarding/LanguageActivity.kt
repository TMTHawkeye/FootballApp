package com.example.footballapp.activities.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.invisible
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityExitScreenBinding
import com.example.footballapp.databinding.ActivityLanguageBinding
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class LanguageActivity : BaseActivity() {
    private lateinit var binding: ActivityLanguageBinding

    private val viewModel: FootballViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentFrom = intent?.getStringExtra("intentFrom")
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

        intentFrom?.let{
            binding.btnBack.visible()
        }?:run{
            binding.btnBack.invisible()
        }

        if (savedInstanceState == null) {
            viewModel.loadMatchesWithStages()
        }

        binding.btnDone.setOnClickListener {

            startActivity(Intent(this@LanguageActivity, MainActivity::class.java))

        }


    }
}