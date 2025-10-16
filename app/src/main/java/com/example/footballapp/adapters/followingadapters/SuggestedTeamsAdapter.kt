/*
package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.interfaces.TeamDiffCallback
import com.example.footballapp.models.followingmodels.Team

class SuggestedTeamsAdapter(
    private val onItemClick: (Team) -> Unit, // For entire item click
    private val onFollowClick: (Team) -> Unit // For follow button click
) : ListAdapter<Team, SuggestedTeamsAdapter.ViewHolder>(TeamDiffCallback()) {

    class ViewHolder(
        private val binding: ItemSuggestedTeamBinding,
        private val onItemClick: (Team) -> Unit,
        private val onFollowClick: (Team) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
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


*/



package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.HomeTeam
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.models.Team
import com.example.footballapp.utils.LeagueListType

class SuggestedTeamsAdapter(
    private val teams: MutableList<Team>,
    private val onItemClick: (Team) -> Unit,
    private val onFollowClick: (Team) -> Unit ,
    private val listType: LeagueListType

) : RecyclerView.Adapter<SuggestedTeamsAdapter.ViewHolder>() {
    private var followedIds: List<String> = emptyList()

    inner class ViewHolder(
        private val binding: ItemSuggestedTeamBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
//            binding.teamImage.setImageResource(team.logo)
            Glide.with(binding.root.context)
                .load(imagePrefix + team.logo)
                .placeholder(R.drawable.app_icon)
                .into(binding.teamImage)


            val isFollowed = followedIds.contains(team.team_id)

            // Update icon color
            binding.followButton.setImageResource(R.drawable.followings)
            binding.followButton.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    if (isFollowed) R.color.green_color else R.color.grey
                )
            )

            binding.followButton.setOnClickListener {
                when (listType) {
                    LeagueListType.SUGGESTED -> {
                        // Toggle follow/unfollow normally
                        onFollowClick(team)
                    }

                    LeagueListType.FOLLOWING -> {
                        // Remove this item from the followed list
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            teams.removeAt(position)
                            notifyItemRemoved(position)
                            onFollowClick(team) // also unfollow in prefs
                        }
                    }
                }
            }

            binding.teamName.text = team.incident_number

            // Entire item click
            binding.root.setOnClickListener { onItemClick(team) }

            // Follow button click
            binding.followButton.setOnClickListener { onFollowClick(team) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSuggestedTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(teams[position])
    }

    override fun getItemCount(): Int = teams.size

     fun updateTeams(newTeams: List<Team>?) {
        teams.clear()
        newTeams?.let { teams.addAll(it) }
        notifyDataSetChanged()
    }

    fun updateFollowedIds(ids: List<String>) {
        followedIds = ids
        notifyDataSetChanged()
    }
    fun addTeam(team: Team) {
        teams.add(team)
        notifyItemInserted(teams.size - 1)
    }

    fun removeTeam(team: Team) {
        val index = teams.indexOf(team)
        if (index != -1) {
            teams.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
