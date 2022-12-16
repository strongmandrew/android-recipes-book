package com.example.recipes_book.adapters.viewHolders

import android.graphics.BitmapFactory
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import com.example.recipes_book.R
import com.google.android.material.imageview.ShapeableImageView

class RecipeViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val imageLoader = ImageLoader(itemView.context)

    val card: CardView = itemView.findViewById(R.id.recipe_card_item)
    val titleField: TextView = itemView.findViewById(R.id.title_text)
    val favouritesButton: CheckBox = itemView.findViewById(R.id.favorites_btn)
    val imageView: ShapeableImageView = itemView.findViewById(R.id.image_view)

    val defaultBitmap = BitmapFactory.decodeResource(
        itemView.resources,
        R.drawable.default_recipe)

}