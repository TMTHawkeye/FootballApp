package com.example.footballapp.adapters.matchadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.Helper.imagePrefixCompetition
import com.example.footballapp.R
import com.example.footballapp.models.matchmodels.Match

class MatchListAdapter(
    val matches: List<Matche>,
    private val onExpandClick: (Matche) -> Unit, // For expand/collapse
    private val onItemClick: (Matche) -> Unit // For navigation
) : RecyclerView.Adapter<MatchListAdapter.MatchListViewHolder>() {

    class MatchListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val matchName: TextView = itemView.findViewById(R.id.competitionName)
        val expandIcon: ImageView = itemView.findViewById(R.id.expandIcon)
        val expandedContent: ViewGroup = itemView.findViewById(R.id.expandedContent)

        // Competition views
        val competitionLogo1: ImageView = itemView.findViewById(R.id.competitionLogoooo)

        // Team views
        val team1Logo: ImageView = itemView.findViewById(R.id.team1Logo)
        val team1Name: TextView = itemView.findViewById(R.id.team1Name)
        val team1Score: TextView = itemView.findViewById(R.id.team1Score)

        val team2Logo: ImageView = itemView.findViewById(R.id.team2Logo)
        val team2Name: TextView = itemView.findViewById(R.id.team2Name)
        val team2Score: TextView = itemView.findViewById(R.id.team2Score)

        // Date and time view
        val date: TextView = itemView.findViewById(R.id.matchDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_single_match, parent, false)
        return MatchListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchListViewHolder, position: Int) {
        val match = matches[position]

        holder.matchName.text = match.tournamentName

//        if (match.isExpanded) {
        holder.expandedContent.visibility = View.VISIBLE
        holder.expandIcon.setImageResource(R.drawable.downarrow)

        // Set competition data
        if (match.tournamentLogo != null) {
            Glide.with(holder.itemView.context)
                .load(imagePrefixCompetition + match.tournamentLogo)
                .into(holder.competitionLogo1)
//                holder.competitionLogo1.setImageResource(match.tournamentLogo)
        }

        // Set team data
        Glide.with(holder.itemView.context)
            .load(imagePrefix + match.home_team?.get(0)?.logo)
            .into(holder.team1Logo)

//            holder.team1Logo.setImageResource(match.home_team?.get(0)?.logo)
        holder.team1Name.text = match.home_team?.get(0)?.incident_number
//            holder.team2Logo.setImageResource(match.team2.logoResId)
        Glide.with(holder.itemView.context)
            .load(imagePrefix + match.away_team?.get(0)?.logo)
            .into(holder.team2Logo)
        holder.team2Name.text = match.away_team?.get(0)?.incident_number

        // Set scores
        if (match.home_score != null && match.away_score != null) {
            holder.team1Score.text = match.home_score.toString()
            holder.team2Score.text = match.away_score.toString()
            holder.team1Score.visibility = View.VISIBLE
            holder.team2Score.visibility = View.VISIBLE
        } else {
            holder.team1Score.visibility = View.GONE
            holder.team2Score.visibility = View.GONE
        }

        // Set date and time
        holder.date.text = "${match.start_datetime}"
//        } else {
//            holder.expandedContent.visibility = View.GONE
//            holder.expandIcon.setImageResource(R.drawable.forwardarrow1)
//        }

        // Set click listener for expand icon (only expands/collapses)
        holder.expandIcon.setOnClickListener {
            onExpandClick(match)
        }

        // Set click listener for the rest of the item (navigates to detail)
        holder.itemView.setOnClickListener {
            onItemClick(match)
        }
    }

    override fun getItemCount(): Int = matches.size
}