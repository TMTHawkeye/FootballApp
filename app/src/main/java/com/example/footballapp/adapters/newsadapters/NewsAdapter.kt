/*
package com.example.footballapp.adapters.newsadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
 import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.R

class NewsAdapter(private val items: MutableList<LatestNewsResponseItem>
) :
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var onItemClickListener: ((LatestNewsResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (LatestNewsResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivNewsImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvNewsTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvNewsDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvNewsDate)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
//                    onItemClickListener?.invoke(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_latest_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = items[position]

         Glide.with(holder.itemView.context)
            .load(item.image)
             .into(  holder.ivImage)
        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.preview
        holder.tvDate.text = item.publish_time

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    fun updateData(newList : List<LatestNewsResponseItem>){
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}*/


package com.example.footballapp.adapters.newsadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapi.modelClasses.latestNews.LatestNewsResponseItem
import com.example.footballapp.R

class NewsAdapter :
    PagingDataAdapter<LatestNewsResponseItem, NewsAdapter.NewsViewHolder>(DiffCallback) {

     private var onItemClickListener: ((LatestNewsResponseItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (LatestNewsResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    // ✅ ViewHolder same as your old one
    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivNewsImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvNewsTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvNewsDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvNewsDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_latest_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val item = getItem(position) ?: return

        Glide.with(holder.itemView.context)
            .load(item.image)
             .into(holder.ivImage)

        holder.tvTitle.text = item.title
        holder.tvDescription.text = item.preview
        holder.tvDate.text = item.publish_time

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(item)
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LatestNewsResponseItem>() {
            override fun areItemsTheSame(
                oldItem: LatestNewsResponseItem,
                newItem: LatestNewsResponseItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LatestNewsResponseItem,
                newItem: LatestNewsResponseItem
            ): Boolean = oldItem == newItem
        }
    }
}
