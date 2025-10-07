package com.example.footballapp.adapters.matchadapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.Helper.formatMatchStatus
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R

// MatchSliderAdapter.kt



class MatchSliderAdapter(
    private val matches: List<Matche>,
    private val onMatchClick: (Matche) -> Unit
) : RecyclerView.Adapter<MatchSliderAdapter.MatchSliderViewHolder>() {


    class MatchSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val team1Logo: ImageView = itemView.findViewById(R.id.team1Logo)
        val team2Logo: ImageView = itemView.findViewById(R.id.team2Logo)
        val competitionLogo: ImageView = itemView.findViewById(R.id.competitionLogo)
        val team1Name: TextView = itemView.findViewById(R.id.team1Name)
        val team2Name: TextView = itemView.findViewById(R.id.team2Name)
        val team1Goals: TextView = itemView.findViewById(R.id.team1Goals)
        val team2Goals: TextView = itemView.findViewById(R.id.team2Goals)
        val matchTime: TextView = itemView.findViewById(R.id.matchTime)
        val competition: TextView = itemView.findViewById(R.id.competition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchSliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match_slider, parent, false)
        return MatchSliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchSliderViewHolder, position: Int) {
        val match = matches[position]

        // Load team logos from drawable resources
//        holder.team1Logo.setImageResource(match.home_team.logoResId)
//        holder.team2Logo.setImageResource(match.team2.logoResId)

        // Load competition logo from drawable resource
        Log.d("TAG_logo", "onBindViewHolder: ${match.tournamentLogo}")

        if (match.tournamentLogo != null) {
            Glide.with(holder.itemView.rootView.context)
                .load(imagePrefix+match.tournamentLogo)
                .placeholder(R.drawable.app_icon).into(holder.competitionLogo)
             holder.competitionLogo.visibility = View.VISIBLE
        } else {
            Glide.with(holder.itemView.rootView.context)
                 .load(R.drawable.app_icon).into(holder.competitionLogo)
            holder.competitionLogo.visibility = View.VISIBLE
            // Hide competition logo if not available
//            holder.competitionLogo.visibility = View.GONE
        }

        holder.team1Name.text = match.home_team?.get(0)?.incident_number
        holder.team2Name.text = match.away_team?.get(0)?.incident_number
        holder.matchTime.text = formatMatchStatus(match.match_status)
//        holder.competition.text = match.tournamentName
        holder.competition.text = match.tournamentName?.replace(" ", "\n")


        // Handle score display
        if (match.home_score != null && match.away_score != null) {
            holder.team1Goals.text = match.home_score.toString()
            holder.team2Goals.text = match.away_score.toString()
            holder.team1Goals.visibility = View.VISIBLE
            holder.team2Goals.visibility = View.VISIBLE
        } else {
            // No score yet, show time instead of goals
            holder.team1Goals.visibility = View.GONE
            holder.team2Goals.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            onMatchClick(match)
        }
    }

    override fun getItemCount(): Int = matches.size



}