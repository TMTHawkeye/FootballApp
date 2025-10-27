package com.example.footballapp.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.R
import com.example.footballapp.activities.onboarding.BaseActivity
import com.example.footballapp.databinding.ActivityNewsDetailBinding
import com.example.footballapp.models.newsmodel.NewsItem
import com.example.footballapp.viewmodels.TeamViewmodel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class NewsDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityNewsDetailBinding

    val teamViewModel : TeamViewmodel by viewModel()
    var newsItem : LatestNewsResponseItem ? =null

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

          binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        // Set up toolbar

        newsItem = teamViewModel.getSelectedNews()
//        Toast.makeText(this@NewsDetailActivity, "${newsItem?.id}", Toast.LENGTH_SHORT).show()

        // Populate the views with news data
        newsItem?.let {
             Glide.with(binding.root.context).load(it.image)
                .into(binding.ivNewsImage)
            binding.tvNewsTitle.text = it.title
            binding.tvNewsDescription.text = it.preview
            binding.tvNewsDate.text = it.publish_time
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
