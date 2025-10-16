/*
package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemTeamBinding
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


class TeamDiffCallback : DiffUtil.ItemCallback<Team1>() {
    override fun areItemsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem == newItem
}*/




package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemTeamBinding
import com.example.footballapp.models.followingmodels.Team1

class FollowingTeamsAdapter(
    private val teams: MutableList<Team1>,
    private val onItemClick: (Team1) -> Unit
) : RecyclerView.Adapter<FollowingTeamsAdapter.ViewHolder>() {

    inner class ViewHolder(
        private val binding: ItemTeamBinding
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
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(teams[position])
    }

    override fun getItemCount(): Int = teams.size

    /** ✅ Optional: to update list dynamically */
    fun updateTeams(newTeams: List<Team1>) {
        teams.clear()
        teams.addAll(newTeams)
        notifyDataSetChanged()
    }
}
