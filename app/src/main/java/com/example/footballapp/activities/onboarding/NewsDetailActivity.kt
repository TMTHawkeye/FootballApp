package com.example.footballapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivityNewsDetailBinding
import com.example.footballapp.models.newsmodel.NewsItem

class NewsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewsDetailBinding.inflate(layoutInflater)
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

        // Get the NewsItem from intent
        val newsItem = intent.getParcelableExtra<NewsItem>("NEWS_ITEM")
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        // Set up toolbar


        // Populate the views with news data
        newsItem?.let {
            binding.ivNewsImage.setImageResource(it.imageResId)
            binding.tvNewsTitle.text = it.title
            binding.tvNewsDescription.text = it.description
            binding.tvNewsDate.text = it.date
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
