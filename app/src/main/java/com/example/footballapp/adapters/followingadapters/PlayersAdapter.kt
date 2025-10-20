package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.teamPlayerStats.Player
import com.example.footballapp.databinding.ItemPlayerBinding

class PlayersAdapter : ListAdapter<Player, PlayersAdapter.ViewHolder>(PlayerDiffCallback()) {

    class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.apply {
                // Set player image
//                ivPlayer.setImageResource(player.teamBadge)
                Glide.with(binding.root.context)
                    .load(player.teamBadge.high)
                    .into(binding.ivPlayer)

                // Set player information
                tvPlayerName.text = player.name

                tvPlayerNumber.text = "#${player.rank}"


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

class PlayerDiffCallback : DiffUtil.ItemCallback<Player>() {
    override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem.playerId == newItem.playerId

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem
}