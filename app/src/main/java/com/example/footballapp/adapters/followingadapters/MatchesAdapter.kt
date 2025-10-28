package com.example.footballapp.adapters.followingadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.teamMatches.Event
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemSingleMatchBinding
import com.example.footballapp.databinding.ItemTextHeaderBinding
import com.example.footballapp.models.Team
import java.text.SimpleDateFormat
import java.util.Locale

class MatchesAdapter(
    private val matches: MutableList<Event> = mutableListOf(),
    private val onMatchClick: ((Event) -> Unit)? = null

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_ALL_MATCHES = 0
        private const val TYPE_MATCH = 1
    }

    // ----- ViewHolders -----
    class AllMatchesViewHolder(private val binding: ItemTextHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.apply {
                textView.text = binding.root.context.getString(R.string.all_matches)
            }
        }
    }

    class MatchViewHolder(private val binding: ItemSingleMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Event) {
            binding.apply {
                // Format date
//                val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
//                val outputFormat = SimpleDateFormat("EEE, MMM dd, hh:mm a", Locale.getDefault())
//
//                val date = inputFormat.parse(match.event_start_datetime.toString())
//                matchDate.text = date?.let { outputFormat.format(it) } ?: match.event_start_datetime.toString()

                val inputFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("hh:mm a'\n'dd MMM, EEE", Locale.getDefault())

                val date = try {
                    inputFormat.parse(match.event_start_datetime.toString())
                } catch (e: Exception) {
                    null
                }

                matchDate.text = date?.let { outputFormat.format(it) } ?: match.event_start_datetime.toString()

                
                Glide.with(binding.root.context)
                    .load(imagePrefix + match.home_team[0].team_badge)
                    .into(team1Logo)

                Glide.with(binding.root.context)
                    .load(imagePrefix + match.away_team[0].team_badge)
                    .into(team2Logo)

                team1Name.text = match.home_team[0].team_name
                team2Name.text = match.away_team[0].team_name


            }


        }
    }

    // ----- Adapter Methods -----
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_ALL_MATCHES else TYPE_MATCH
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ALL_MATCHES) {
            val binding = ItemTextHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            AllMatchesViewHolder(binding)
        } else {
            val binding = ItemSingleMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MatchViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MatchViewHolder) {
            holder.bind(matches[position - 1]) // offset by 1 because first is "All Matches"
            holder.itemView.rootView.setOnClickListener {
                onMatchClick?.invoke(matches.get(position-1))
            }
        } else if (holder is AllMatchesViewHolder) {
            holder.bind()
        }
    }

    override fun getItemCount(): Int = matches.size + 1 // +1 for "All Matches"

    // ----- Public Setter -----
    fun setMatches(newMatches: List<Event>) {
        matches.clear()
        matches.addAll(newMatches)
        notifyDataSetChanged()
    }
}
