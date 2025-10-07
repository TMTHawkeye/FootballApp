package com.example.footballapp.fragments.matchdetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.footballapp.R
import com.example.footballapp.adapters.matchadapters.TableAdapter
import com.example.footballapp.databinding.FragmentTableBinding
import com.example.footballapp.models.matchmodels.Match
import com.example.footballapp.models.matchmodels.TableItem

class TableFragment : Fragment() {

    private lateinit var binding: FragmentTableBinding
//    private lateinit var match: Match

    companion object {
        fun newInstance(match: Match?): TableFragment {
            val fragment = TableFragment()
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
        binding = FragmentTableBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up win probability
//        setupWinProbability()
//
//        // Set up league table - replace with your actual data
//        setupLeagueTable()
    }

    private fun setupWinProbability() {
        // Sample win probability data
        val team1WinProbability = 45
        val team2WinProbability = 30
        val drawProbability = 25


    }

    private fun setupLeagueTable() {
        // Sample table data - replace with your actual data
//        val tableItems = listOf(
//            TableItem(1, "Team A", 20, 15, 3, 2, 28, 48, R.drawable.app_icon),
//            TableItem(2, "Team B", 20, 14, 4, 2, 24, 46, R.drawable.app_icon),
//            TableItem(3, match.team1.name, 20, 13, 5, 2, 21, 44, R.drawable.app_icon, true, false),
//            TableItem(4, "Team D", 20, 12, 4, 4, 16, 40, R.drawable.app_icon),
//            TableItem(5, match.team2.name, 20, 11, 5, 4, 10, 38, R.drawable.app_icon, false, true),
//            TableItem(1, "Team A", 20, 15, 3, 2, 28, 48, R.drawable.app_icon),
//            TableItem(2, "Team B", 20, 14, 4, 2, 24, 46, R.drawable.app_icon),
//            TableItem(3, match.team1.name, 20, 13, 5, 2, 21, 44, R.drawable.app_icon, true, false),
//            TableItem(4, "Team D", 20, 12, 4, 4, 16, 40, R.drawable.app_icon),
//            TableItem(5, match.team2.name, 20, 11, 5, 4, 10, 38, R.drawable.app_icon, false, true),
//            TableItem(1, "Team A", 20, 15, 3, 2, 28, 48, R.drawable.app_icon),
//            TableItem(2, "Team B", 20, 14, 4, 2, 24, 46, R.drawable.app_icon),
//            TableItem(3, match.team1.name, 20, 13, 5, 2, 21, 44, R.drawable.app_icon, true, false),
//            TableItem(4, "Team D", 20, 12, 4, 4, 16, 40, R.drawable.app_icon),
//            TableItem(5, match.team2.name, 20, 11, 5, 4, 10, 38, R.drawable.app_icon, false, true),
//            TableItem(1, "Team A", 20, 15, 3, 2, 28, 48, R.drawable.app_icon),
//            TableItem(2, "Team B", 20, 14, 4, 2, 24, 46, R.drawable.app_icon),
//            TableItem(3, match.team1.name, 20, 13, 5, 2, 21, 44, R.drawable.app_icon, true, false),
//            TableItem(4, "Team D", 20, 12, 4, 4, 16, 40, R.drawable.app_icon),
//            TableItem(5, match.team2.name, 20, 11, 5, 4, 10, 38, R.drawable.app_icon, false, true)
//        )
//
//        val adapter = TableAdapter(tableItems)
//        binding.leagueTableRecyclerView.adapter = adapter
//        binding.leagueTableRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
}