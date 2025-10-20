package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.modelClasses.teamMatches.Event
import com.example.footballapp.databinding.ItemStandingBinding

class StandingsAdapter : RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {

    private val standings = mutableListOf<Event>()

    inner class ViewHolder(private val binding: ItemStandingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(standing: Event) {
            binding.apply {
//                tvPosition.text = standing.position.toString()
//                ivTeamLogo.setImageResource(standing.teamIcon)
//                tvTeamName.text = standing.teamName
//                tvPlayed.text = standing.played.toString()
//                tvWon.text = standing.won.toString()
//                tvLost.text = standing.lost.toString()
//                tvDrawn.text = standing.drawn.toString()
//                tvGoalDifference.text = standing.goalDifference.toString()
//                tvPoints.text = standing.points.toString()
//
//                // 🎨 Example: highlight top 3 teams
//                when (standing.position) {
//                    1 -> root.setBackgroundColor(root.context.getColor(android.R.color.holo_green_light))
//                    2 -> root.setBackgroundColor(root.context.getColor(android.R.color.holo_blue_light))
//                    3 -> root.setBackgroundColor(root.context.getColor(android.R.color.holo_orange_light))
//                    else -> root.setBackgroundColor(root.context.getColor(android.R.color.transparent))
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStandingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = standings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(standings[position])
    }

    // 🔹 Update full list
    fun setData(newList: List<Event>) {
        standings.clear()
        standings.addAll(newList)
        notifyDataSetChanged()
    }

    // 🔹 Optional: Update a single item
    fun updateItem(position: Int, updatedEvent: Event) {
        if (position in standings.indices) {
            standings[position] = updatedEvent
            notifyItemChanged(position)
        }
    }

    // 🔹 Optional: Add more standings
    fun addMore(newItems: List<Event>) {
        val start = standings.size
        standings.addAll(newItems)
        notifyItemRangeInserted(start, newItems.size)
    }
}
