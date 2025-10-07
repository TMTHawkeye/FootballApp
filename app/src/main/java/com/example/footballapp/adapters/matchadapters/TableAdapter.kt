package com.example.footballapp.adapters.matchadapters

import android.content.res.Resources
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.fragments.matchdetail.TableFragment
import com.example.footballapp.models.matchmodels.TableItem

class TableAdapter(private val tableItems: List<TableItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    class TableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val position: TextView = itemView.findViewById(R.id.positionn)
        val teamLogo: ImageView = itemView.findViewById(R.id.teamLogoo)
        val teamName: TextView = itemView.findViewById(R.id.teamName)
        val matchesPlayed: TextView = itemView.findViewById(R.id.matchesPlayedd)
        val wins: TextView = itemView.findViewById(R.id.winss)
        val draws: TextView = itemView.findViewById(R.id.drawss)
        val losses: TextView = itemView.findViewById(R.id.lossess)
        val goalDifference: TextView = itemView.findViewById(R.id.goalDifferencee)
        val points: TextView = itemView.findViewById(R.id.pointss)
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.table_header_row, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_table_row, parent, false)
            TableViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TableViewHolder) {
            val tableItem = tableItems[position - 1] // Adjust for header

            holder.position.text = tableItem.position.toString()

            // Set team logo with error handling
            try {
                holder.teamLogo.setImageResource(tableItem.teamLogo)
            } catch (e: Resources.NotFoundException) {
                holder.teamLogo.setImageResource(R.drawable.app_icon)
            }

            holder.teamName.text = tableItem.teamName
            holder.matchesPlayed.text = tableItem.matchesPlayed.toString()
            holder.wins.text = tableItem.wins.toString()
            holder.draws.text = tableItem.draws.toString()
            holder.losses.text = tableItem.losses.toString()

            // Format goal difference with +/-
            val goalDiffText = if (tableItem.goalDifference >= 0) {
                "+${tableItem.goalDifference}"
            } else {
                tableItem.goalDifference.toString()
            }
            holder.goalDifference.text = goalDiffText

            holder.points.text = tableItem.points.toString()

            // Highlight special positions



            // Make current teams bold
            if (tableItem.isCurrentTeam1 || tableItem.isCurrentTeam2) {
                holder.teamName.setTypeface(null, Typeface.BOLD)
                holder.points.setTypeface(null, Typeface.BOLD)
            }
        }
        // Header doesn't need binding
    }

    override fun getItemCount(): Int = tableItems.size + 1 // +1 for header

    private fun highlightPosition(holder: TableViewHolder, colorRes: Int, isChampion: Boolean) {
        val context = holder.itemView.context
        val color = ContextCompat.getColor(context, colorRes)

        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green_color))

        if (isChampion) {
            holder.position.setTypeface(null, Typeface.BOLD)
            holder.position.setTextColor(color)
        }
    }

    private fun resetHighlight(holder: TableViewHolder) {
        val context = holder.itemView.context
        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        holder.position.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        holder.position.setTypeface(null, Typeface.NORMAL)
    }
}