package com.example.footballapp.adapters.matchadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.modelClasses.matchSummary.Events
import com.example.footballapi.sealedClasses.MatchEvent
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemEventBinding

class EventAdapter(private val events: List<MatchEvent>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    lateinit var binding: ItemEventBinding


    class EventViewHolder(var binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.binding.eventTimee.text = event.minute.toString()
//        holder.binding.eventTypee.text = event
//        holder.binding.eventDescriptionn.text = event.player
//        holder.binding.teamLogoo.setImageResource(event.teamLogo)


        with(holder.binding) {
            when (event) {
                is MatchEvent.GoalEvent -> {
                    eventTypee.text = "Goal"
                    eventDescriptionn.text = "${event.player} (${event.team})"
                    eventIconn.setImageResource(R.drawable.app_icon) // your goal icon
                }

                is MatchEvent.YellowCardEvent -> {
                    eventTypee.text = "Yellow Card"
                    eventDescriptionn.text = "${event.player} (${event.team})"
                    eventIconn.setImageResource(R.drawable.playerr)
                }

                is MatchEvent.RedCardEvent -> {
                    eventTypee.text = "Red Card"
                    eventDescriptionn.text = "${event.player} (${event.team})"
                    eventIconn.setImageResource(R.drawable.redplayer)
                }

                is MatchEvent.SubstitutionEvent -> {
                    eventTypee.text = "Substitution"
                    eventDescriptionn.text =
                        "${event.playerOut} ➜ ${event.playerIn} (${event.team})"
                    eventIconn.setImageResource(R.drawable.app_icon)
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return events.size
    }

}