package com.example.footballapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.Stage
import com.example.footballapp.Helper.ApiResultTAG
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemStageBinding
import com.example.footballapp.interfaces.OnMatchSelected
import com.example.footballapp.interfaces.OnStageClickListener

class StageAdapter(
    private val stages: MutableList<Stage>,
    private val listener: OnStageClickListener,
    private val matchSelectedListener : OnMatchSelected
) : RecyclerView.Adapter<StageAdapter.StageViewHolder>() {

    // Keep track of which items are expanded
    private val expandedPositions = mutableSetOf<Int>()

    init {
        // Expand first 5 items by default
        for (i in 0 until minOf(5, stages.size)) {
            expandedPositions.add(i)
        }
    }

    inner class StageViewHolder(val binding: ItemStageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stage: Stage) = with(binding) {
            val compName = stage.competition_name?.takeIf { it != "null" } ?: ""
            val stageName = stage.stage_name?.takeIf { it != "null" } ?: ""

            val displayName = when {
                compName.equals(stageName, ignoreCase = true) -> compName // if both same
                compName.isNotBlank() && stageName.isNotBlank() -> "$compName - $stageName"
                compName.isNotBlank() -> compName
                stageName.isNotBlank() -> stageName
                else -> "Unknown Competition"
            }

            competitionNAme.text = displayName


            Glide.with(competitionLogoooo.context)
                .load(imagePrefix+stage.badge_url)
                .placeholder(R.drawable.app_icon)
                .into(competitionLogoooo)

            // Setup matches recycler
            val matchAdapter = MatchesAdapter(mutableListOf(),matchSelectedListener)
            matchesRecycler.layoutManager = LinearLayoutManager(root.context)
            matchesRecycler.adapter = matchAdapter

            // Show expanded if this position is in expandedPositions
            val isExpanded = expandedPositions.contains(position)
            matchesRecycler.visibility = if (isExpanded) View.VISIBLE else View.GONE
            expandIcon.rotation = if (isExpanded) 90f else 0f


            if (isExpanded) {
                stage.matches?.let { matchAdapter.updateData(it) }
            }

            // Initially collapsed
//            matchesRecycler.visibility = View.GONE

            expandIcon.setOnClickListener {
                val currentlyExpanded = expandedPositions.contains(position)

                if (currentlyExpanded) {
                    expandedPositions.remove(position)
                    matchesRecycler.visibility = View.GONE
                    expandIcon.animate().rotation(0f).start()
                } else {
                    expandedPositions.add(position)
                    matchesRecycler.visibility = View.VISIBLE
                    expandIcon.animate().rotation(90f).start()

                    // Load matches dynamically on expand
                    stage.matches?.let { newData -> matchAdapter.updateData(newData) }
                    listener.onStageExpanded(stage)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StageViewHolder {
        val binding =
            ItemStageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StageViewHolder, position: Int) {
        holder.bind(stages[position])
    }

    override fun getItemCount(): Int = stages.size

    fun updateData(newData: List<Stage>) {
        Log.d(ApiResultTAG, "updateData: ${newData.size}")
        stages.clear()
        stages.addAll(newData)

        // Reset expanded state → expand first 5 of new data
        expandedPositions.clear()
        for (i in 0 until minOf(5, newData.size)) {
            expandedPositions.add(i)
        }

        notifyDataSetChanged()
    }
}
