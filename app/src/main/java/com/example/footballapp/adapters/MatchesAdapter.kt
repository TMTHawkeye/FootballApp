package com.example.footballapp.adapters


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.Matche
import com.example.footballapp.Helper.MATCH_ID
import com.example.footballapp.Helper.imagePrefix
import com.example.footballapp.R
import com.example.footballapp.activities.MatchDetailActivity
import com.example.footballapp.databinding.ItemSingleMatchBinding
import com.example.footballapp.interfaces.OnMatchSelected
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MatchesAdapter(
    private val matches: MutableList<Matche>,
    private val onMatchClicked: (Matche) -> Unit
//    private val matchSelectedListener : OnMatchSelected
) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    inner class MatchViewHolder(val binding: ItemSingleMatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(match: Matche) = with(binding) {


            matchDate.text = match.start_datetime ?: ""

            team1Name.text = match.home_team?.get(0)?.incident_number ?: "Team A"
            team2Name.text = match.away_team?.get(0)?.incident_number ?: "Team B"

            team1Name.isSelected = true
            team2Name.isSelected = true

            binding.matchDate.text = formatMatchInfo(binding,match.match_status, match.start_datetime)

            Log.d("TAG_teamLogo", "bind: ${imagePrefix + match.home_team?.get(0)?.logo}")
            Log.d("TAG_teamLogo", "bind1: ${imagePrefix + match.away_team?.get(0)?.logo}")
            Glide.with(team1Logo.context)
                .load(imagePrefix + match.home_team?.get(0)?.logo)
                .placeholder(R.drawable.app_icon)
                .into(team1Logo)

            Glide.with(team2Logo.context)
                .load(imagePrefix + match.away_team?.get(0)?.logo)
                .placeholder(R.drawable.app_icon)
                .into(team2Logo)

            if (match != null) {
                team1Score.text = match.home_score?.toString() ?: "-"
                team2Score.text = match.away_score?.toString() ?: "-"
                team1Score.visibility = View.VISIBLE
                team2Score.visibility = View.VISIBLE
            } else {
                team1Score.visibility = View.GONE
                team2Score.visibility = View.GONE
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val binding =
            ItemSingleMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matches[position])
        holder.binding.divider.visibility =
            if (matches.size > 1 && position != matches.size - 1) View.VISIBLE
            else View.GONE

        holder.itemView.setOnClickListener {
//            matchSelectedListener.onMatchClicked(matches[position])
            onMatchClicked.invoke(matches[position])
            holder.binding.root.context.startActivity(
                Intent(
                    holder.binding.root.context,
                    MatchDetailActivity::class.java
                ).putExtra(MATCH_ID, matches[position].match_id)
            )
        }
    }

    override fun getItemCount(): Int = matches.size

    fun updateData(newData: List<Matche>) {
        matches.clear()
        matches.addAll(newData)
        notifyDataSetChanged()
    }


    /*   private fun formatMatchInfo(status: String?, date: String?): String {
           val safeStatus = status?.takeIf { it.isNotBlank() && it != "null" }
           val safeDate = date?.takeIf { it.isNotBlank() && it != "null" }

           return listOfNotNull(safeStatus, safeDate).joinToString("\n")
       }*/

    private fun formatMatchInfo(binding : ItemSingleMatchBinding,status: String?, date: String?): String {
        val safeStatus = status?.takeIf { it.isNotBlank() && it != "null" }

        val safeDate = date?.takeIf { it.isNotBlank() && it != "null" }?.let {
            try {
                val inputFormatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val localDateTime = LocalDateTime.parse(it, inputFormatter)

                val today = LocalDate.now()
                val matchDate = localDateTime.toLocalDate()

                if (matchDate.isEqual(today)) {
                    binding.root.context.getString(R.string.today)
                } else {
                    val outputFormatter =
                        DateTimeFormatter.ofPattern("dd MMM, EEE", Locale.getDefault())
                    localDateTime.format(outputFormatter)   // 👉 "26 Aug, Wed"
                }
            } catch (e: Exception) {
                null // fallback if parsing fails
            }
        }

        return listOfNotNull(safeStatus, safeDate).joinToString("\n")
    }

}
