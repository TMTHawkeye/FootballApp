package com.example.footballapp.adapters.followingadapters

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
import com.example.footballapi.modelClasses.AllCompetitions.Stage
import com.example.footballapp.Helper.gone
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.Helper.imagePrefixCompetition
import com.example.footballapp.Helper.visible
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemFollowedGroupBinding
import com.example.footballapp.databinding.ItemFollowingHeaderBinding
import com.example.footballapp.databinding.ItemSuggestedTeamBinding
import com.example.footballapp.databinding.ItemTextHeaderBinding
import com.example.footballapp.models.Team

class SuggestedLeaguesAdapter(
    private val ctxt : Context,
    private val shouldAddTags: Boolean = true,
    private val onFollowClick: (Stage) -> Unit,
    private val onItemClick: (Stage) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var isExpanded = true

    private val items = mutableListOf<Any>()
    private val originalItems = mutableListOf<Any>()

    companion object {
        private const val TYPE_FOLLOWED_GROUP = 0
        private const val TYPE_HEADER_MORE = 1
        private const val TYPE_LEAGUE_MORE = 2
    }

    fun setData(followedLeagues: List<Stage>, moreLeagues: List<Stage>) {
        items.clear()

        if (followedLeagues.isNotEmpty()) {
            items.add(followedLeagues)
        }
        if (shouldAddTags) {
            items.add(ctxt.getString(R.string.follow_more))
        }
        items.addAll(moreLeagues)
        originalItems.addAll(items)

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = items[position]) {
            is List<*> -> Companion.TYPE_FOLLOWED_GROUP
            is String -> Companion.TYPE_HEADER_MORE
            else -> TYPE_LEAGUE_MORE
        }
    }

    override fun getItemCount() = items.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_FOLLOWED_GROUP -> FollowedGroupViewHolder(
                ItemFollowedGroupBinding.inflate(inflater, parent, false)
            )

            TYPE_HEADER_MORE -> HeaderViewHolder(
                ItemTextHeaderBinding.inflate(inflater, parent, false)
            )

            else -> LeagueViewHolder(
                ItemSuggestedTeamBinding.inflate(inflater, parent, false)
            )
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is List<*> -> (holder as FollowedGroupViewHolder).bind(item.filterIsInstance<Stage>())
            is String -> (holder as HeaderViewHolder).bind(item)
            is Stage -> (holder as LeagueViewHolder).bind(item)
        }
    }


    private inner class HeaderViewHolder(private val binding: ItemTextHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String) {
            binding.textView.text = title
        }
    }

  /*  inner class FollowedGroupViewHolder(private val binding: ItemFollowedGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(followedTeams: List<Stage>) {
            binding.followedContainer.removeAllViews()

            if (shouldAddTags) {
                // 1️⃣ Add the header INSIDE the group
                val headerBinding = ItemTextHeaderBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.followedContainer,
                    false
                )

//                val headerTitle = "Following | ${followedTeams.size}"
                val headerTitle =
                    binding.root.context.getString(R.string.following_header, followedTeams.size)
                val spannable = SpannableString(headerTitle)
                val start = headerTitle.indexOf("|") + 1
                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(binding.root.context, R.color.green_color)
                    ),
                    start,
                    headerTitle.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                headerBinding.textView.text = spannable
                binding.followedContainer.addView(headerBinding.root)
            }
            // 2️⃣ Add each followed team under it
            followedTeams.forEach { league ->
                val itemBinding = ItemSuggestedTeamBinding.inflate(
                    LayoutInflater.from(binding.root.context),
                    binding.followedContainer,
                    false
                )
                itemBinding.teamName.text = league.competition_name

                Glide.with(itemBinding.teamImage)
                    .load(imagePrefixCompetition + league.badge_url)
                    .into(itemBinding.teamImage)

                // Change button color or icon for followed
                itemBinding.followButton.setImageResource(R.drawable.floow)

                itemBinding.root.setOnClickListener { onItemClick(league) }
                itemBinding.followButton.setOnClickListener { onFollowClick(league) }

                binding.followedContainer.addView(itemBinding.root)
            }
        }
    }
*/

    private inner class FollowedGroupViewHolder(private val binding: ItemFollowedGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(followedTeams: List<Stage>) {
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

                    itemBinding.teamName.text = team.competition_name
                    Glide.with(itemBinding.teamImage)
                        .load(imagePrefixCompetition + team.badge_url)
                        .into(itemBinding.teamImage)

                    itemBinding.followButton.setImageResource(R.drawable.floow)

                    itemBinding.root.setOnClickListener { onItemClick(team) }
                    itemBinding.followButton.setOnClickListener { onFollowClick(team) }



                    if ((index == followedTeams.size - 1)) {
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
    inner class LeagueViewHolder(val binding: ItemSuggestedTeamBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stage: Stage) {
            if(!shouldAddTags) {
                binding.followButton.gone()
            }
            else{
                binding.followButton.visible()
            }
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

//            val isFollowed = followedIds.contains(stage.stage_id)
//
//            // Update icon color
//            binding.followButton.setImageResource(R.drawable.followings)
//            binding.followButton.setColorFilter(
//                ContextCompat.getColor(
//                    binding.root.context,
//                    if (isFollowed) R.color.green_color else R.color.grey
//                )
//            )
            binding.followButton.setOnClickListener { onFollowClick(stage) }


            // ✅ Hide divider for the last item
            val isLastItem = adapterPosition == itemCount - 1
            binding.view3.visibility = if (isLastItem) View.GONE else View.VISIBLE

             if (!shouldAddTags) {
                binding.view3.visibility  = View.INVISIBLE
            }


            binding.root.setOnClickListener {
                onItemClick.invoke(stage)
            }
        }
    }


    fun addMoreLeagues(newLeague: List<Stage>) {
        if (newLeague.isEmpty()) return

        // Find where "Follow More" section starts
        val headerIndex = items.indexOfFirst { it is String && it == ctxt.getString(R.string.follow_more)}
        val insertStart = if (headerIndex != -1) headerIndex + 1 else items.size

        items.addAll(insertStart, newLeague)
        originalItems.addAll(insertStart, newLeague)

        notifyItemRangeInserted(insertStart, newLeague.size)
    }

    fun filterList(filteredTeams: List<Stage>) {
        items.clear()

        // When a search query is active, we only show the flat filtered list
        items.addAll(filteredTeams)

        notifyDataSetChanged()
    }
}
