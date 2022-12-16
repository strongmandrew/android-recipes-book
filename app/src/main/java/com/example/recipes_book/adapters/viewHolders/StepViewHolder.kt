package com.example.recipes_book.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R

class StepViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val stepNumber: TextView = itemView.findViewById(R.id.step_number)
    val step : TextView = itemView.findViewById(R.id.step_text)
}