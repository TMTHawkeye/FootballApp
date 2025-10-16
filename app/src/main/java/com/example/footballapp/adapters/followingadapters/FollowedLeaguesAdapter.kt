package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.Helper.imagePrefixCompetition
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.databinding.ItemTeamBinding
import com.example.footballapp.utils.LeagueListType

class FollowedLeaguesAdapter(
    private val leagues: MutableList<Stage>,
    private val onFollowClick: (Stage) -> Unit,
    private val listType: LeagueListType
) : RecyclerView.Adapter<FollowedLeaguesAdapter.LeagueViewHolder>() {




    private var followedIds: List<String> = emptyList()

    fun updateLeagues(newLeagues: List<Stage>) {
        leagues.clear()
        leagues.addAll(newLeagues)
        notifyDataSetChanged()
    }

    fun updateFollowedIds(ids: List<String>) {
        followedIds = ids
        notifyDataSetChanged()
    }

    inner class LeagueViewHolder(val binding: ItemTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stage: Stage) {
            Glide.with(binding.root.context)
                .load(imagePrefixCompetition + stage.badge_url)
                .placeholder(R.drawable.app_icon)
                .into(binding.teamImage)

            binding.teamName.text =
                if (stage.competition_name != "null" && !stage.competition_name.isNullOrBlank()) {
                    stage.competition_name
                } else {
                    stage.stage_name
                }

            val isFollowed = followedIds.contains(stage.stage_id)

            // Update icon color
            binding.teamImage1.setImageResource(R.drawable.followings)
            binding.teamImage1.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    if (isFollowed) R.color.green_color else R.color.grey
                )
            )

            // ✅ Different behavior based on list type
            binding.teamImage1.setOnClickListener {
                when (listType) {
                    LeagueListType.SUGGESTED -> {
                        // Toggle follow/unfollow normally
                        onFollowClick(stage)
                    }

                    LeagueListType.FOLLOWING -> {
                        // Remove this item from the followed list
                        val position = adapterPosition
                        if (position != RecyclerView.NO_POSITION) {
                            leagues.removeAt(position)
                            notifyItemRemoved(position)
                            onFollowClick(stage) // also unfollow in prefs
                        }
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {
        val binding = ItemTeamBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeagueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bind(leagues[position])
    }

    override fun getItemCount() = leagues.size
}
