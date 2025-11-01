package com.example.footballapp.adapters


import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemFollowedGroupBinding
import com.example.footballapp.databinding.ItemFollowingHeaderBinding
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.databinding.ItemTextHeaderBinding
import com.example.footballapp.models.Team

class TeamsAdapter(
    private val ctxt : Context,
    private val shouldAddTags: Boolean = true,
    private val onItemClick: (Team) -> Unit,
    private val onFollowToggle: (Team) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isExpanded = true

    private val items = mutableListOf<Any>()
    private val originalItems = mutableListOf<Any>()

    companion object {
        private const val TYPE_FOLLOWED_GROUP = 0
        private const val TYPE_HEADER_MORE = 1
        private const val TYPE_TEAM_MORE = 2
    }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FOLLOWED_GROUP -> FollowedGroupViewHolder(
                ItemFollowedGroupBinding.inflate(inflater, parent, false)
            )

            TYPE_HEADER_MORE -> HeaderViewHolder(
                ItemTextHeaderBinding.inflate(inflater, parent, false)
            )

            else -> TeamViewHolder(
                ItemSuggestedTeamBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {

            is List<*> -> (holder as FollowedGroupViewHolder).bind(item.filterIsInstance<Team>())
            is String -> (holder as HeaderViewHolder).bind(item)
            is Team -> (holder as TeamViewHolder).bind(item)
        }
    }


    private inner class HeaderViewHolder(private val binding: ItemTextHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.textView.text = title
        }
    }

    inner class TeamViewHolder(private val binding: ItemSuggestedTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(team: Team) {
            if(!shouldAddTags) {
                binding.followButton.gone()
            }
            else{
                binding.followButton.visible()
            }

            binding.teamName.text = team.incident_number
            Glide.with(binding.root.context)
                .load(imagePrefix + team.logo)
                .into(binding.teamImage)

            binding.root.setOnClickListener { onItemClick(team) }
            binding.followButton.setOnClickListener { onFollowToggle(team) }


            // ✅ Hide divider for the last item
            val isLastItem = adapterPosition == itemCount - 1
            binding.view3.visibility = if (isLastItem) View.GONE else View.VISIBLE

            if (!shouldAddTags) {
                binding.view3.visibility  = View.INVISIBLE
            }

        }
    }

    fun addMoreTeams(newTeams: List<Team>) {
        if (newTeams.isEmpty()) return

        // Find where "Follow More" section starts
        val headerIndex = items.indexOfFirst { it is String && it == ctxt.getString(R.string.follow_more) }
        val insertStart = if (headerIndex != -1) headerIndex + 1 else items.size

        items.addAll(insertStart, newTeams)
        originalItems.addAll(insertStart, newTeams)
        notifyItemRangeInserted(insertStart, newTeams.size)
    }


    fun setData(followedTeams: List<Team>, moreTeams: List<Team>) {
        items.clear()
        originalItems.clear()

        // Build the default grouped structure
        if (followedTeams.isNotEmpty()) {
            items.add(followedTeams)
        }

        if (shouldAddTags) {
            items.add(ctxt.getString(R.string.follow_more))
        }
        items.addAll(moreTeams)

        // Keep a copy of the original grouped list
        originalItems.addAll(items)

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is List<*> -> TYPE_FOLLOWED_GROUP
            is String -> TYPE_HEADER_MORE
            else -> TYPE_TEAM_MORE
        }
    }


    fun filterList(filteredTeams: List<Team>) {
        items.clear()

        // When a search query is active, we only show the flat filtered list
        items.addAll(filteredTeams)

        notifyDataSetChanged()
    }




    private inner class FollowedGroupViewHolder(private val binding: ItemFollowedGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(followedTeams: List<Team>) {



            binding.followedContainer.removeAllViews()

            // Inflate new header with arrow
            val headerBinding = ItemFollowingHeaderBinding.inflate(
                LayoutInflater.from(binding.root.context),
                binding.followedContainer,
                false
            )

            val title = binding.root.context.getString(R.string.following_header, followedTeams.size)
            headerBinding.tvHeaderTitle.text = title

            // Rotate arrow based on expanded state
//            headerBinding.imgArrow.rotation = if (isExpanded) 90f else 0f
            headerBinding.imgArrow.animate().setDuration(150).rotation(if (isExpanded) 90f else 0f)

            // Add header to parent
            binding.followedContainer.addView(headerBinding.root)

            // Add Teams only if expanded
            if (isExpanded) {
                followedTeams.forEachIndexed { index, team ->
                    val itemBinding = ItemSuggestedTeamBinding.inflate(
                        LayoutInflater.from(binding.root.context),
                        binding.followedContainer,
                        false
                    )

                    itemBinding.teamName.text = team.incident_number
                    Glide.with(itemBinding.teamImage)
                        .load(imagePrefix + team.logo)
                        .into(itemBinding.teamImage)

                    itemBinding.followButton.setImageResource(R.drawable.floow)

                    itemBinding.root.setOnClickListener { onItemClick(team) }
                    itemBinding.followButton.setOnClickListener { onFollowToggle(team) }


                    if (index == followedTeams.size - 1) {
                        itemBinding.view3.visibility = View.GONE
                    } else {
                        itemBinding.view3.visibility = View.VISIBLE
                    }


                    binding.followedContainer.addView(itemBinding.root)


                }
            }

            // Toggle expand/collapse on header click
            headerBinding.root.setOnClickListener {
                isExpanded = !isExpanded
                notifyItemChanged(adapterPosition)
            }
        }
    }

}
