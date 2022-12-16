package com.example.recipes_book.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R

class ChipViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val chip: TextView = itemView.findViewById(R.id.chip_text)

}