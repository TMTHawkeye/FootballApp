package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemStandingBinding
import com.example.footballapp.fragments.Standing

class StandingsAdapter : ListAdapter<Standing, StandingsAdapter.ViewHolder>(StandingDiffCallback()) {

    class ViewHolder(private val binding: ItemStandingBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(standing: Standing) {
            binding.apply {
                tvPosition.text = standing.position.toString()
                ivTeamLogo.setImageResource(standing.teamIcon)
                tvTeamName.text = standing.teamName
                tvPlayed.text = standing.played.toString()
                tvWon.text = standing.won.toString()
                tvLost.text = standing.lost.toString()
                tvDrawn.text = standing.drawn.toString()
                tvGoalDifference.text = standing.goalDifference.toString()
                tvPoints.text = standing.points.toString()

                // Highlight top positions with different colors




            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStandingBinding.inflate(
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

class StandingDiffCallback : DiffUtil.ItemCallback<Standing>() {
    override fun areItemsTheSame(oldItem: Standing, newItem: Standing): Boolean = 
        oldItem.teamName == newItem.teamName
    
    override fun areContentsTheSame(oldItem: Standing, newItem: Standing): Boolean = 
        oldItem == newItem
}