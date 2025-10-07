package com.example.footballapp.fragments.matchdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R


import com.example.footballapp.databinding.FragmentLineupBinding
import com.example.footballapp.models.matchmodels.Match

class LineupFragment : Fragment() {

    private lateinit var binding: FragmentLineupBinding
//    private lateinit var match: Match

    companion object {
        fun newInstance(match: Match?): LineupFragment {
            val fragment = LineupFragment()
            val args = Bundle()
            args.putSerializable("MATCH", match)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        match = arguments?.getSerializable("MATCH") as Match
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLineupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set team names
      /*  binding.team1Name.text = match.team1.name
        binding.team2Name.text = match.team2.name*/



        // Set up lineups

    }









}