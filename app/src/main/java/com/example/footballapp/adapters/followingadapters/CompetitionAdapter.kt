package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemCompetitionBinding
import com.example.footballapp.models.followingmodels.Competition

class CompetitionAdapter(private val onItemClick: (Competition, Int) -> Unit) :
    ListAdapter<Competition, CompetitionAdapter.ViewHolder>(CompetitionDiffCallback()) {

    private var selectedPosition = 0 // Track the selected position

    class ViewHolder(
        private val binding: ItemCompetitionBinding,
        private val onItemClick: (Competition, Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(competition: Competition) {
            binding.competitionName.text = competition.name
            binding.root.setOnClickListener {
                onItemClick(competition, adapterPosition)
            }

            // Change background based on selection state
            if (competition.isSelected) {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.selectedtab
                )
                binding.competitionName.setTextColor(
                    ContextCompat.getColor(binding.root.context, android.R.color.white)
                )
            } else {
                binding.root.background = ContextCompat.getDrawable(
                    binding.root.context,
                    R.drawable.rounded_tab22
                )
                binding.competitionName.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.grey)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCompetitionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val competition = getItem(position).copy(isSelected = position == selectedPosition)
        holder.bind(competition)
    }

    // Update selection when an item is clicked
    fun updateSelectedPosition(position: Int) {
        val previousSelected = selectedPosition
        selectedPosition = position
        notifyItemChanged(previousSelected)
        notifyItemChanged(selectedPosition)
    }
}

class CompetitionDiffCallback : DiffUtil.ItemCallback<Competition>() {
    override fun areItemsTheSame(oldItem: Competition, newItem: Competition): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Competition, newItem: Competition): Boolean =
        oldItem == newItem
}