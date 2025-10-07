package com.example.footballapp.fragments.boardingfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.footballapp.databinding.FragmentFirstOnBoardingBinding

class FirstOnBoardingFragment : Fragment() {

    lateinit var  binding : FragmentFirstOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstOnBoardingBinding.inflate(layoutInflater)
        return binding.root
     }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



    }

 }