package com.example.footballapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
 import androidx.viewpager2.widget.ViewPager2
import com.example.footballapp.R
import com.example.footballapp.databinding.FragmentFollowingBinding
 import com.example.footballapp.viewmodels.SearchSharedViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var viewPagerAdapter: FollowingPagerAdapter
    private val sharedViewModel: SearchSharedViewModel by activityViewModel()


    var isSearchVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()

        binding.refresh.setOnClickListener {
            if (!isSearchVisible) {
                 binding.title.animate().alpha(0f).setDuration(200).withEndAction {
                    binding.title.visibility = View.GONE
                    binding.refresh.visibility = View.GONE

                    binding.searchInputLayout.visibility = View.VISIBLE
                    binding.searchInputLayout.alpha = 0f
                    binding.searchInputLayout.animate().alpha(1f).setDuration(200).start()

                     binding.searchEditText.requestFocus()
                     val imm = binding.refresh.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                     imm.showSoftInput(binding.searchEditText, InputMethodManager.SHOW_IMPLICIT)
                }.start()
            }
            isSearchVisible = true
        }

        binding.searchInputLayout.setStartIconOnClickListener {
          makeSearchHide()
        }


        binding.searchEditText.addTextChangedListener { editable ->
            sharedViewModel.updateSearchQuery(editable.toString())
        }



    }

    private fun makeSearchHide() {
        binding.searchInputLayout.animate().alpha(0f).setDuration(200).withEndAction {
            binding.searchInputLayout.visibility = View.GONE

            binding.title.visibility = View.VISIBLE
            binding.title.alpha = 0f
            binding.title.animate().alpha(1f).setDuration(200).start()

            binding.refresh.visibility = View.VISIBLE
            binding.refresh.alpha = 0f
            binding.refresh.animate().alpha(1f).setDuration(200).start()
        }.start()

        binding.searchEditText.clearFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        clearSearch()
        binding.searchEditText.text = null
        isSearchVisible = false
    }

    private fun clearSearch() {
        binding.searchEditText.setText("")
        sharedViewModel.updateSearchQuery("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
         clearSearch()
    }
    private fun setupViewPager() {
        viewPagerAdapter = FollowingPagerAdapter(childFragmentManager)
        binding.viewPager.adapter = viewPagerAdapter
        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        val tabTitles = arrayOf(getString(R.string.teams), getString(R.string.leagues_head))
        val tabIcons = arrayOf(R.drawable.teamm, R.drawable.leaguee)

        for (i in 0 until binding.tabLayout.tabCount) {
            val tab = binding.tabLayout.getTabAt(i)
            val customView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_tab, null)

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
                0 -> TeamsFragment()
                1 -> LeaguesFragment()
                else -> TeamsFragment()
            }
        }
    }
}