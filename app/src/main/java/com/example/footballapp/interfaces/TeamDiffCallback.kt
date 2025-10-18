package com.example.footballapp.interfaces

import androidx.recyclerview.widget.DiffUtil
import com.example.footballapp.models.Team

class TeamItemCallback : DiffUtil.ItemCallback<Team>() {
    override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
        // identify item by stable id
        return oldItem.team_id == newItem.team_id
    }

    override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
        // use data class equality OR compare specific fields
        return oldItem == newItem
        // or if you want a cheaper check:
        // return oldItem.incident_number == newItem.incident_number
        //        && oldItem.logo == newItem.logo
        //        && oldItem.primary_color == newItem.primary_color
    }
}

