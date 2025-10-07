package com.example.footballapp.adapters.matchadapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.databinding.ItemStageBinding
import com.example.footballapp.databinding.ItemStatBinding
import com.example.footballapp.models.matchmodels.Stat

class StatAdapter(private val stats: List<Stat>) :
    RecyclerView.Adapter<StatAdapter.StatViewHolder>() {

       lateinit var binding : ItemStatBinding
    class StatViewHolder(var binding: ItemStatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        binding = ItemStatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return StatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = stats[position]
        
        holder.binding.statName.text = stat.name
        holder.binding.team1StatValue.text = stat.team1Value.toString()
        holder.binding.team2StatValue.text = stat.team2Value.toString()
        
        // Calculate progress percentage for the progress bar
        val total = stat.team1Value + stat.team2Value
        val progress = if (total > 0) {
            (stat.team1Value.toFloat() / total * 100).toInt()
        } else {
            50 // Default to 50% if both values are 0
        }
        


        // Set different progress drawable based on stat type if needed
   /*     when (stat.name) {
            "Possession" -> holder.statProgress.progressDrawable = 
                holder.itemView.context.getDrawable(R.drawable.progress_possession)
            "Shots" -> holder.statProgress.progressDrawable = 
                holder.itemView.context.getDrawable(R.drawable.progress_shots)
            "Shots on Target" -> holder.statProgress.progressDrawable = 
                holder.itemView.context.getDrawable(R.drawable.progress_shots_on_target)
            else -> holder.statProgress.progressDrawable = 
                holder.itemView.context.getDrawable(R.drawable.progress_stat)
        }*/
    }

    override fun getItemCount(): Int = stats.size
}