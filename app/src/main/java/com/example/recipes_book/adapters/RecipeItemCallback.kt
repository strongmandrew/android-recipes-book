package com.example.recipes_book.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.recipes_book.models.room.Recipe

class RecipeItemCallback: DiffUtil.ItemCallback<Recipe>() {

    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.isFavourite == newItem.isFavourite
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}