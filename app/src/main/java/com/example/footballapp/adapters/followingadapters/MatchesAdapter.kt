package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemSingleMatchBinding
import com.example.footballapp.models.followingmodels.Match1

import java.text.SimpleDateFormat
import java.util.Locale

class MatchesAdapter : ListAdapter<Match1, MatchesAdapter.ViewHolder>(MatchDiffCallback()) {

    class ViewHolder(private val binding: ItemSingleMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(match: Match1) {
            binding.apply {
                // Format date
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val outputFormat = SimpleDateFormat("EEE, MMM dd", Locale.getDefault())
                val date = inputFormat.parse(match.date)
                matchDate.text = date?.let { outputFormat.format(it) } ?: match.date

                // Set team information
                team1Logo.setImageResource(match.homeTeamIcon)
                team2Logo.setImageResource(match.awayTeamIcon)
                team1Name.text = match.homeTeam
                team2Name.text = match.awayTeam

            /*    // Set match status and display accordingly
                if (match.status == "FT") { // Full Time
                    tvMatchScore.text = match.score ?: "2-1"
                    tvMatchScore.visibility = View.VISIBLE
                    tvMatchTime.visibility = View.GONE
                    tvMatchStatus.text = "FT"
                } else if (match.status == "LIVE") {
                    tvMatchScore.text = match.score ?: "1-0"
                    tvMatchScore.visibility = View.VISIBLE
                    tvMatchTime.visibility = View.GONE
                    tvMatchStatus.text = "LIVE"
                    tvMatchStatus.setTextColor(root.context.getColor(android.R.color.holo_red_dark))
                } else { // Upcoming match
                    tvMatchScore.visibility = View.GONE
                    tvMatchTime.visibility = View.VISIBLE
                    tvMatchTime.text = match.time
                    tvMatchStatus.text = "UPCOMING"
                    tvMatchStatus.setTextColor(root.context.getColor(android.R.color.darker_gray))
                }

                // Set venue
                tvVenue.text = match.venue ?: "${match.homeTeam} Stadium"*/
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSingleMatchBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class MatchDiffCallback : DiffUtil.ItemCallback<Match1>() {
    override fun areItemsTheSame(oldItem: Match1, newItem: Match1): Boolean =
        oldItem.id == newItem.id
    
    override fun areContentsTheSame(oldItem: Match1, newItem: Match1): Boolean =
        oldItem == newItem
}