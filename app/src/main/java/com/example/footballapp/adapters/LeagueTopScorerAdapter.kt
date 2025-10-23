package com.example.footballapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.leaguePlayerStats.Player
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.databinding.ItemPlayerBinding

class LeagueTopScorerAdapter : ListAdapter<Player, LeagueTopScorerAdapter.ViewHolder>(PlayerDiffCallback()) {

    class ViewHolder(private val binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(player: Player) {
            binding.apply {
                // Set player image
//                ivPlayer.setImageResource(player.teamBadge)
                Glide.with(binding.root.context)
                    .load(imagePrefix+player.team_badge)
                    .into(binding.ivPlayer)

                // Set player information
                tvPlayerName.text = player.player_name

                tvPlayerNumber.text = when {
                    !player.scores?.`1`.isNullOrEmpty() -> player.scores?.`1`
                    !player.scores?.`3`.isNullOrEmpty() -> player.scores?.`3`
                    !player.scores?.`4`.isNullOrEmpty() -> player.scores?.`4`
                    !player.scores?.`6`.isNullOrEmpty() -> player.scores?.`6`
                    !player.scores?.`8`.isNullOrEmpty() -> player.scores?.`8`
                    else -> "0"
                }



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
        oldItem.player_id == newItem.player_id

    override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean =
        oldItem == newItem
}