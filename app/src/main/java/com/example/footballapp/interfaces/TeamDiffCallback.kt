package com.example.footballapp.interfaces

import androidx.recyclerview.widget.DiffUtil
import com.example.footballapp.models.followingmodels.Team1

class TeamDiffCallback : DiffUtil.ItemCallback<Team1>() {
    override fun areItemsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Team1, newItem: Team1): Boolean = oldItem == newItem
}