package com.example.footballapp.fragments.boardingfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.footballapp.databinding.FragmentThirdOnBoardingBinding

class ThirdOnBoardingFragment : Fragment() {

    lateinit var binding : FragmentThirdOnBoardingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdOnBoardingBinding.inflate(layoutInflater)
        return binding.root
      }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

 }