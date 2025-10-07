package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemTopPlayerBinding
import com.example.footballapp.fragments.TopPlayer

class TopPlayersAdapter : ListAdapter<TopPlayer, TopPlayersAdapter.ViewHolder>(TopPlayerDiffCallback()) {

    class ViewHolder(private val binding: ItemTopPlayerBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(player: TopPlayer) {
            binding.ivPlayer.setImageResource(player.image)
            binding.tvPlayerName.text = player.name
            binding.tvPlayerNumber.text = player.goals.toString()
            
            // Set position number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTopPlayerBinding.inflate(
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

class TopPlayerDiffCallback : DiffUtil.ItemCallback<TopPlayer>() {
    override fun areItemsTheSame(oldItem: TopPlayer, newItem: TopPlayer): Boolean = 
        oldItem.name == newItem.name
    
    override fun areContentsTheSame(oldItem: TopPlayer, newItem: TopPlayer): Boolean = 
        oldItem == newItem
}