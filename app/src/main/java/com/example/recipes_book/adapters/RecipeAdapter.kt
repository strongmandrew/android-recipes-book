package com.example.recipes_book.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.example.recipes_book.R
import com.example.recipes_book.models.Recipe
import com.google.android.material.imageview.ShapeableImageView

class RecipeAdapter: RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var recipes = listOf<Recipe>()
    set(value) {
        val callback = RecipeDiffCallback(recipes, value)
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this)
        field = value
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleField: TextView = itemView.findViewById(R.id.title_text)
        val favouritesButton: ImageButton = itemView.findViewById(R.id.favorites_btn)
        val imageView: ShapeableImageView = itemView.findViewById(R.id.image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleField.text = recipes[position].title

        holder.favouritesButton.setOnClickListener {
            val newState = !recipes[position].isFavourite

            recipes[position].isFavourite = newState
        }

        Glide.with(holder.itemView)
            .load(recipes[position].imageUrl)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = recipes.size

    override fun getItemViewType(position: Int): Int {

        return if (recipes[position].isFavourite) R.layout.recipe_favourite_item
        else R.layout.recipe_main_item

    }
}