package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemTeamBinding
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.models.followingmodels.Team1


class FollowingTeamsAdapter(private val onItemClick: (Team1) -> Unit) :
    ListAdapter<Team1, FollowingTeamsAdapter.ViewHolder>(TeamDiffCallback()) {

    class ViewHolder(
        private val binding: ItemTeamBinding,
        private val onItemClick: (Team1) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team1) {
            binding.teamImage.setImageResource(team.imageRes)
            binding.teamName.text = team.name
            binding.root.setOnClickListener { onItemClick(team) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SuggestedTeamsAdapter(
    private val onItemClick: (Team1) -> Unit, // For entire item click
    private val onFollowClick: (Team1) -> Unit // For follow button click
) : ListAdapter<Team1, SuggestedTeamsAdapter.ViewHolder>(TeamDiffCallback()) {

    class ViewHolder(
        private val binding: ItemSuggestedTeamBinding,
        private val onItemClick: (Team1) -> Unit,
        private val onFollowClick: (Team1) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team1) {
            binding.teamImage.setImageResource(team.imageRes)
            binding.teamName.text = team.name

            // Set click listener for the entire item
            binding.root.setOnClickListener { onItemClick(team) }

            // Set click listener for the follow button
            binding.followButton.setOnClickListener { onFollowClick(team) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestedTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick, onFollowClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TeamDiffCallback : DiffUtil.ItemCallback<Team1>() {
    override fun areItemsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem == newItem
}