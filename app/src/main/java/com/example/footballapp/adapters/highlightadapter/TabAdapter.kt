// adapters/TabAdapter.kt
package com.example.footballapp.adapters.highlightadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.footballapp.R

import com.example.footballapp.models.highlightmodel.TabItem

class TabAdapter(
    private val tabs: List<TabItem>,
    private val onTabClick: (TabItem) -> Unit
) : RecyclerView.Adapter<TabAdapter.TabViewHolder>() {

    class TabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tabText: TextView = itemView.findViewById(R.id.dateText)
        val cardView: CardView = itemView.findViewById(R.id.dateCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TabViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_date, parent, false)
        return TabViewHolder(view)
    }

    override fun onBindViewHolder(holder: TabViewHolder, position: Int) {
        val tab = tabs[position]

        holder.tabText.text = tab.title

        if (tab.isSelected) {
            holder.cardView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.selectedtab
            )
            holder.tabText.setTextColor(
                ContextCompat.getColor(holder.itemView.context, android.R.color.white)
            )
        } else {
            holder.cardView.background = ContextCompat.getDrawable(
                holder.itemView.context,
                R.drawable.rounded_tab22
            )
            holder.tabText.setTextColor(
                ContextCompat.getColor(holder.itemView.context, R.color.grey)
            )
        }

        holder.itemView.setOnClickListener {
            onTabClick(tab)
        }
    }

    override fun getItemCount(): Int = tabs.size

    fun updateSelection(selectedTab: TabItem) {
        tabs.forEach { it.isSelected = it.id == selectedTab.id }
        notifyDataSetChanged()
    }
}