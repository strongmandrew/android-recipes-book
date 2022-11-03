package com.example.recipes_book.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.recipes_book.models.room.Recipe

class RecipeDiffCallback(
    private val oldList: List<Recipe>,
    private val newList: List<Recipe>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldId = oldList[oldItemPosition].id
        val newId = newList[newItemPosition].id
        return oldId == newId
    }
}