package com.example.footballapp.adapters.matchadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapi.modelClasses.matchSummary.Events
import com.example.footballapi.sealedClasses.MatchEvent
import com.example.footballapp.Helper.gone
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

        holder.binding.eventTimee.text = event.minute.toString()+"'"
//        holder.binding.eventTypee.text = event
//        holder.binding.eventDescriptionn.text = event.player
//        holder.binding.teamLogoo.setImageResource(event.teamLogo)


        with(holder.binding) {
            when (event) {
                is MatchEvent.GoalEvent -> {
                    eventTypee.text = "${event.player} from ${event.team}"
                    eventDescriptionn.text = "Goal"
                    eventIconn.setImageResource(R.drawable.goal_logo) // your goal icon
                }

                is MatchEvent.YellowCardEvent -> {
                    eventTypee.text = "${event.player} from ${event.team}"
                    eventDescriptionn.text = "Foul"
                    eventIconn.setImageResource(R.drawable.yellow_card)
                }

                is MatchEvent.RedCardEvent -> {
                    eventTypee.text = "${event.player} from ${event.team}"
                    eventDescriptionn.text = "Foul"
                    eventIconn.setImageResource(R.drawable.red_card)
                }

                is MatchEvent.SubstitutionEvent -> {
                    eventTypee.text =  "${event.playerOut} ➜ ${event.playerIn} (${event.team})"
                    eventDescriptionn.gone()
                }
            }
        }


    }

    override fun getItemCount(): Int {
        return events.size
    }

}