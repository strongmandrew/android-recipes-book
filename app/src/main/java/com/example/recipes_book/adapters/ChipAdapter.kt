package com.example.recipes_book.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R
import com.example.recipes_book.adapters.viewHolders.ChipViewHolder

class ChipAdapter(private val diets: List<String>, private val color: Int): RecyclerView.Adapter<ChipViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chip_item, parent, false)
        return ChipViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        holder.chip.text = diets[position]
        holder.chip.setBackgroundColor(color)
    }

    override fun getItemCount(): Int = diets.size
}