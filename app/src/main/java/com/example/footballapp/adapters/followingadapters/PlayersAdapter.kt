package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.modelClasses.teamMatches.Stage
import com.example.footballapp.databinding.ItemPlayerBinding

class PlayersAdapter : ListAdapter<Stage, PlayersAdapter.ViewHolder>(PlayerDiffCallback()) {

    class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Stage) {
            binding.apply {
                // Set player image
//                ivPlayer.setImageResource(player.image)
//
//                // Set player information
//                tvPlayerName.text = player.name
//
//                tvPlayerNumber.text = "#${player.number}"


                // Highlight important players


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlayerBinding.inflate(
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

class PlayerDiffCallback : DiffUtil.ItemCallback<Stage>() {
    override fun areItemsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem.stage_id == newItem.stage_id

    override fun areContentsTheSame(oldItem: Stage, newItem: Stage): Boolean =
        oldItem == newItem
}