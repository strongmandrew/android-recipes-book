package com.example.recipes_book.adapters.viewHolders

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R
import com.google.android.material.imageview.ShapeableImageView

class SimilarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val card: CardView = itemView.findViewById(R.id.similar_card)
    val title: TextView = itemView.findViewById(R.id.similar_title)
    val image: ShapeableImageView = itemView.findViewById(R.id.similar_image)
}