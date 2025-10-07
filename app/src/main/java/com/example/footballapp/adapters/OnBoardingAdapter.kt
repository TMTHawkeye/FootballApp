package com.example.footballapp.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.footballapp.fragments.boardingfragments.FirstOnBoardingFragment
import com.example.footballapp.fragments.boardingfragments.SecondOnBoardingFragment
import com.example.footballapp.fragments.boardingfragments.ThirdOnBoardingFragment


class OnBoardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstOnBoardingFragment()
            1 -> SecondOnBoardingFragment()
            2 -> ThirdOnBoardingFragment()
            else -> FirstOnBoardingFragment() // fallback
        }
    }
}

