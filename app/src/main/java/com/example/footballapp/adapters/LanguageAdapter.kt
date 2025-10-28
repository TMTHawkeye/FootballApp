package com.example.footballapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.footballapp.R
import com.example.footballapp.databinding.ItemLanguageBinding
import com.example.footballapp.interfaces.SelectedLanguageCallback
import com.example.footballapp.models.LanguageModel

class LanguageAdapter(
    val context: Context,
    val languageList: ArrayList<LanguageModel>,
    var selectedPosition: Int?,
    val callback: SelectedLanguageCallback
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    var languageCode = "en"
    var savedPosition = selectedPosition
    var positionListner: SelectedLanguageCallback = callback

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = languageList.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val item = languageList[position]
        val binding = holder.binding

        binding.tvCountryName.text = item.languageName
        binding.tvSelectCountryName.text = item.languageName

        languageList.get(selectedPosition ?: 0).languageCode?.let { changeLanguage(it) }

        holder.itemView.setOnClickListener {
             languageList.get(position).languageCode?.let { it1 -> changeLanguage(it1) }
            updateSelectedPosition(position)

        }

        holder.binding.radioBtn.setOnClickListener {
             languageList.get(position).languageCode?.let { it1 -> changeLanguage(it1) }
            updateSelectedPosition(position)
        }


        if (position == selectedPosition) {
             binding.cdLanguage.strokeColor =
                ContextCompat.getColor(context, R.color.green1_color)   // Selected stroke
            binding.radioBtn.isChecked = true
            binding.radioBtn.buttonTintList =
                ContextCompat.getColorStateList(context, R.color.green1_color) // radio tint
        } else {
             binding.cdLanguage.strokeColor =
                ContextCompat.getColor(context, R.color.grey)  // Normal stroke
            binding.radioBtn.isChecked = false
            binding.radioBtn.buttonTintList =
                ContextCompat.getColorStateList(context, R.color.grey) // radio tint
        }

        Glide.with(binding.root.context).load(languageList.get(position).languageDrawable)
            .into(binding.ivCountry)

    }

    private fun updateSelectedPosition(position: Int) {
        selectedPosition = position
        positionListner.languageSelected(position)

        notifyDataSetChanged()
    }

    fun changeLanguage(languageCode: String) {
        this.languageCode = languageCode
    }
}
