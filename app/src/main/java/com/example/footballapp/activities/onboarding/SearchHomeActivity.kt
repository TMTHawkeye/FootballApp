package com.example.footballapp.activities.onboarding

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.databinding.ActivitySearchHomeBinding
import com.example.footballapp.fragments.FollowingFragment.FollowingPagerAdapter
import com.example.footballapp.fragments.LeaguesFragment
import com.example.footballapp.fragments.TeamsFragment
import com.example.footballapp.fragments.searchFragments.SearchLeaguesFragment
import com.example.footballapp.fragments.searchFragments.SearchTeamsFragment
import com.example.footballapp.viewmodels.SearchSharedViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class SearchHomeActivity : BaseActivity() {
    lateinit var binding : ActivitySearchHomeBinding

    private lateinit var viewPagerAdapter: FollowingPagerAdapter
    private val sharedViewModel: SearchSharedViewModel by viewModel()

    var isSearchVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        setupViewPager()

        binding.searchEditText.addTextChangedListener { editable ->
            sharedViewModel.updateSearchQuery(editable.toString())
        }

        binding.searchInputLayout.setStartIconOnClickListener {
            makeSearchHide()
        }


    }

    private fun setupViewPager() {
        viewPagerAdapter = FollowingPagerAdapter(supportFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val tabTitles = arrayOf(" Teams", " Leagues")
        val tabIcons = arrayOf(R.drawable.teamm, R.drawable.leaguee)

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val customView = LayoutInflater.from(this@SearchHomeActivity).inflate(R.layout.custom_tab, null)

            val tabIcon = customView.findViewById<ImageView>(R.id.tabIcon)
            val tabText = customView.findViewById<TextView>(R.id.tabText)

            tabIcon.setImageResource(tabIcons[i])
            tabText.text = tabTitles[i]

            tab?.customView = customView
        }
    }

    inner class FollowingPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 2

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> SearchTeamsFragment()
                1 -> SearchLeaguesFragment()
                else -> SearchTeamsFragment()
            }
        }
    }

    override fun finish() {
        super.finish()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // API 34
            overrideActivityTransition(
                Activity.OVERRIDE_TRANSITION_CLOSE,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        } else {
            @Suppress("DEPRECATION")
            overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
        }
    }


    private fun makeSearchHide() {

        binding.searchEditText.clearFocus()
        val imm = this@SearchHomeActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

        binding.searchEditText.text = null
        isSearchVisible = false
        clearSearch()
        finish()
    }



    private fun clearSearch() {
        binding.searchEditText.setText("")
        sharedViewModel.updateSearchQuery("")
    }

    override fun onStop() {
        super.onStop()
        clearSearch()
    }



}