package com.example.footballapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.sealedClasses.sealedTableItem
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R

class LeagueTableAdapter(private val items: List<sealedTableItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_LEAGUE_DIVIDER = 0
        private const val TYPE_HEADER_ROW = 1
        private const val TYPE_TEAM_ROW = 2
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view)

    inner class DividerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val leagueName: TextView = view.findViewById(R.id.leagueName)
    }

    inner class TeamViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val position: TextView = view.findViewById(R.id.positionn)
        val teamLogo: ImageView = view.findViewById(R.id.teamLogoo)
        val teamName: TextView = view.findViewById(R.id.teamName)
        val matchesPlayed: TextView = view.findViewById(R.id.matchesPlayedd)
        val wins: TextView = view.findViewById(R.id.winss)
        val draws: TextView = view.findViewById(R.id.drawss)
        val losses: TextView = view.findViewById(R.id.lossess)
        val goalDifference: TextView = view.findViewById(R.id.goalDifferencee)
        val points: TextView = view.findViewById(R.id.pointss)
    }

    override fun getItemViewType(position: Int): Int = when (items[position]) {
        is sealedTableItem.LeagueDivider -> TYPE_LEAGUE_DIVIDER
        is sealedTableItem.HeaderRow -> TYPE_HEADER_ROW
        is sealedTableItem.TeamRow -> TYPE_TEAM_ROW
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_LEAGUE_DIVIDER -> {
                val view = inflater.inflate(R.layout.item_league_divider, parent, false)
                DividerViewHolder(view)
            }
            TYPE_HEADER_ROW -> {
                val view = inflater.inflate(R.layout.item_table_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_table_row, parent, false)
                TeamViewHolder(view)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is sealedTableItem.LeagueDivider -> {
                val dividerHolder = holder as DividerViewHolder
                dividerHolder.leagueName.text = item.leagueName
            }
            is sealedTableItem.TeamRow -> {
                val teamHolder = holder as TeamViewHolder

                teamHolder.position.text = item.position.toString()
                teamHolder.teamName.text = item.teamName
                teamHolder.matchesPlayed.text = item.matchesPlayed.toString()
                teamHolder.wins.text = item.wins.toString()
                teamHolder.draws.text = item.draws.toString()
                teamHolder.losses.text = item.losses.toString()
                teamHolder.goalDifference.text = item.goalDifference.toString()
//                    if (item.goalDifference >= 0) "+${item.goalDifference}" else item.goalDifference.toString()
                teamHolder.points.text = item.points.toString()

                Glide.with(teamHolder.teamLogo)
                    .load( imagePrefix+item.teamLogo)
                    .placeholder(R.drawable.app_icon)
                    .into(teamHolder.teamLogo)
            }
            else->{

            }
        }
    }

    override fun getItemCount(): Int = items.size
}