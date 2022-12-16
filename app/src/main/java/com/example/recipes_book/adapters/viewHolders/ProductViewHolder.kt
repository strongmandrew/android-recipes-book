package com.example.recipes_book.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R

class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val product: TextView = itemView.findViewById(R.id.product_text)
}