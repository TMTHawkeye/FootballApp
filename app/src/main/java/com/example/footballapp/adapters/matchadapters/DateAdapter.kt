package com.example.footballapp.adapters.matchadapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R
import com.example.footballapp.models.matchmodels.MatchDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateAdapter(
      val dates: MutableList<MatchDate>,
      val onDateClick: (MatchDate) -> Unit
) : RecyclerView.Adapter<DateAdapter.DateViewHolder>() {

    inner class DateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateDayText: TextView = itemView.findViewById(R.id.dateText)
        val cardView: CardView = itemView.findViewById(R.id.dateCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return DateViewHolder(view)
    }

     override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]

//        holder.dateDayText.text = date.displayText

        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        // Show "Today" if this date is today
        holder.dateDayText.text = if (date.fullDate == today) {
            holder.itemView.rootView.context.getString(R.string.today)
        } else {
            date.displayText
        }

        if (date.isSelected) {
            holder.cardView.background = ContextCompat.getDrawable(
                holder.itemView.context, R.drawable.selectedtab
            )
            holder.dateDayText.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.white)
            )
        } else {
            holder.cardView.background = ContextCompat.getDrawable(
                holder.itemView.context, R.drawable.rounded_tab22
            )
            holder.dateDayText.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.grey)
            )
        }

        holder.itemView.setOnClickListener {
            onDateClick(date)
            updateSelection(position)

        }
    }

    override fun getItemCount(): Int = dates.size

    /** Update which date is selected */
    fun updateSelection(newPos: Int) {
        val oldPos = dates.indexOfFirst { it.isSelected }
        if (oldPos != -1) {
            dates[oldPos] = dates[oldPos].copy(isSelected = false)
            notifyItemChanged(oldPos)
        }
        dates[newPos] = dates[newPos].copy(isSelected = true)
        Log.d("TAG_DATE", "Selected Date1: ${dates[newPos]}")
        notifyItemChanged(newPos)
    }

    /** Append new dates for pagination */
    fun addDates(newDates: List<MatchDate>, atStart: Boolean) {
        if (atStart) {
            dates.addAll(0, newDates)
            notifyItemRangeInserted(0, newDates.size)
        } else {
            val start = dates.size
            dates.addAll(newDates)
            notifyItemRangeInserted(start, newDates.size)
        }
    }
}
