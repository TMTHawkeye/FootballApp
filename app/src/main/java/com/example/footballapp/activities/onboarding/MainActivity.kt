package com.example.footballapp.activities.onboarding

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
import com.example.footballapi.FootballViewModel
import com.example.footballapp.Helper.noInternetDialog
import com.example.footballapp.Helper.setupNoInternetDialog
import com.example.footballapp.R
import com.example.footballapp.adapters.HomeAdapter
import com.example.footballapp.databinding.ActivityMainBinding
import com.example.footballapp.utils.NetworkConnectionLiveData
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    var homeAdapter: HomeAdapter? = null

    val footballViewModel: FootballViewModel by viewModel()


    private val exitScreenLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedPosition = result.data?.getIntExtra("fragment_position", -1)
            if (selectedPosition != null && selectedPosition != -1) {
                // Move to the selected fragment
                binding.viewPager2.currentItem = selectedPosition

                // Sync bottom navigation selection
                when (selectedPosition) {
                    0 -> binding.bottomNavigationView.menu.findItem(R.id.menu_home).isChecked = true
                    1 -> binding.bottomNavigationView.menu.findItem(R.id.menu_following).isChecked = true
                    2 -> binding.bottomNavigationView.menu.findItem(R.id.menu_shorts).isChecked = true
                    3 -> binding.bottomNavigationView.menu.findItem(R.id.menu_highlights).isChecked = true
                    4 -> binding.bottomNavigationView.menu.findItem(R.id.menu_news).isChecked = true
                }
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true // For dark icons (use with light backgrounds)
            // OR
            isAppearanceLightStatusBars = false // For light icons (use with dark backgrounds)
        }


        footballViewModel.loadMatchesWithStages()


        homeAdapter = HomeAdapter(this)

        binding.viewPager2.adapter = homeAdapter
        binding.viewPager2.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT_DEFAULT)
        binding.viewPager2.currentItem = 0
        binding.bottomNavigationView.menu.findItem(R.id.menu_home).isChecked = true
        binding.viewPager2.isUserInputEnabled = false

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> binding.viewPager2.currentItem = 0
                R.id.menu_following -> binding.viewPager2.currentItem = 1
                R.id.menu_shorts -> binding.viewPager2.currentItem = 2
                R.id.menu_highlights -> binding.viewPager2.currentItem = 3
                R.id.menu_news -> binding.viewPager2.currentItem = 4
            }
            true
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // Set bottom navigation selection
                when (position) {
                    0 -> binding.bottomNavigationView.menu.findItem(R.id.menu_home).isChecked = true
                    1 -> binding.bottomNavigationView.menu.findItem(R.id.menu_following).isChecked = true
                    2 -> binding.bottomNavigationView.menu.findItem(R.id.menu_shorts).isChecked = true
                    3 -> binding.bottomNavigationView.menu.findItem(R.id.menu_highlights).isChecked = true
                    4 -> binding.bottomNavigationView.menu.findItem(R.id.menu_news).isChecked = true
                }
            }
        })

        binding.imageView.setOnClickListener {
            binding.viewPager2.currentItem = 2
        }
    }

    override fun onBackPressed() {
        if (binding.viewPager2.currentItem != 0) {
            binding.viewPager2.currentItem = 0
        } else {
//            startActivity(Intent(this@MainActivity, ExitScreen::class.java))

            val intent = Intent(this, ExitScreen::class.java)
            exitScreenLauncher.launch(intent)

        }
    }


}