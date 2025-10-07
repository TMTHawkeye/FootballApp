package com.example.footballapp.activities.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.adapters.OnBoardingAdapter
import com.example.footballapp.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding: ActivityOnBoardingBinding

    var adapter: OnBoardingAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
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


        setViewPagerAdapter()

        binding.btnNext.setOnClickListener {
            onNext()
        }

        binding.tvSkip.setOnClickListener {
            onNext()
        }

    }

    fun setViewPagerAdapter() {
        adapter = OnBoardingAdapter(this)
        binding.vpOnboarding.adapter = adapter

        binding.dotsIndicator.attachTo(binding.vpOnboarding)


        binding.vpOnboarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                val lastPosition = binding.vpOnboarding.adapter?.itemCount?.minus(1) ?: 0
                if (position == lastPosition) {
                    binding.btnNext.setText(getString(R.string.continueTV))
                 }
                else{
                    binding.btnNext.setText(getString(R.string.next))

                }
            }
        })


    }

    fun onNext() {
        val viewPager = binding.vpOnboarding
        val lastPosition = viewPager.adapter?.itemCount?.minus(1) ?: 0

        if (viewPager.currentItem == lastPosition) {
            startActivity(Intent(this@OnBoardingActivity, LanguageActivity::class.java))
        } else {
            binding.vpOnboarding.currentItem += 1

        }

    }
}