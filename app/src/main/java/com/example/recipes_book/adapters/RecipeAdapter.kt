package com.example.recipes_book.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.load
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.recipes_book.R
import com.example.recipes_book.adapters.viewHolders.RecipeViewHolder
import com.example.recipes_book.models.room.Recipe
import kotlinx.coroutines.*

const val TAG = "RecipeAdapter"

class RecipeAdapter(private var recipes: ArrayList<Recipe>, private val onFavouritesClick: FavouritesClickListener): RecyclerView.Adapter<RecipeViewHolder>() {

    interface FavouritesClickListener {
        fun onItemClick(view: View, recipe: Recipe)
        fun onAddClick(recipe: Recipe, position: Int)
        fun onDeleteClick(recipe: Recipe, position: Int, adapter: RecipeAdapter)
    }

    fun submitList(newRecipes: ArrayList<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.d(TAG, "onCreateViewHolder call")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder call")
        holder.titleField.text = recipes[position].title

        setupFavouritesButton(holder, position)


        CoroutineScope(Dispatchers.IO).launch {


            val glide = Glide.with(holder.imageView.context)
                .load(recipes[position].imageUrl)

                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                .placeholder(R.drawable.default_recipe)
                .error(R.drawable.default_recipe)

            withContext(Dispatchers.Main) {
                glide.into(holder.imageView)
            }


        }

        setCardMargins(position, holder)



    }

    fun removeRecipe(recipe: Recipe, position: Int) {
        recipes.remove(recipe)
        notifyItemRemoved(position)
    }

    private fun setupFavouritesButton(
        holder: RecipeViewHolder,
        position: Int
    ) {
        holder.favouritesButton.isChecked = recipes[position].isFavourite

        holder.favouritesButton.setOnClickListener {
            val newState = !recipes[position].isFavourite
            recipes[position].isFavourite = newState
            holder.favouritesButton.isChecked = newState

            if (newState) onFavouritesClick.onAddClick(recipes[position], position)
            else onFavouritesClick.onDeleteClick(recipes[position], position, this)
        }

        holder.itemView.setOnClickListener {
            onFavouritesClick.onItemClick(it, recipes[position])
        }
    }

    private fun setCardMargins(
        position: Int,
        holder: RecipeViewHolder
    ) {
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        when (position) {
            0 -> params.setMargins(40, 40, 40, 10)
            recipes.size - 1 -> params.setMargins(40, 10, 40, 220)
            else -> params.setMargins(40, 10, 40, 10)
        }

        holder.card.layoutParams = params
    }

    override fun getItemCount(): Int = recipes.size

}