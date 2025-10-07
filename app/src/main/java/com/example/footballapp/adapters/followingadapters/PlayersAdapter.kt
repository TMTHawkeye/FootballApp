package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemPlayerBinding
import com.example.footballapp.fragments.Player1

class PlayersAdapter : ListAdapter<Player1, PlayersAdapter.ViewHolder>(PlayerDiffCallback()) {

    class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player1) {
            binding.apply {
                // Set player image
                ivPlayer.setImageResource(player.image)

                // Set player information
                tvPlayerName.text = player.name

                tvPlayerNumber.text = "#${player.number}"


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

class PlayerDiffCallback : DiffUtil.ItemCallback<Player1>() {
    override fun areItemsTheSame(oldItem: Player1, newItem: Player1): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Player1, newItem: Player1): Boolean =
        oldItem == newItem
}