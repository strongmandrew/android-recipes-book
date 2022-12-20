package com.example.recipes_book.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
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
        fun onItemClick(recipe: Recipe)
        fun onAddClick(recipe: Recipe, position: Int, adapter: RecipeAdapter)
        fun onDeleteClick(recipe: Recipe, position: Int, adapter: RecipeAdapter)
    }

    fun submitList(newRecipes: ArrayList<Recipe>) {
        recipes = newRecipes
        notifyDataSetChanged()
    }

    fun itemHasBeenChanged(position: Int) {
        notifyItemChanged(position)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.d(TAG, "onCreateViewHolder call")

        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        Log.d(TAG, "onBindViewHolder call")
        holder.titleField.text = recipes[position].title

        setupFavouritesButton(holder, position)

        holder.itemView.setOnClickListener {
            onFavouritesClick.onItemClick(recipes[position])
        }

        CoroutineScope(Dispatchers.IO).launch {


            val glide = async {

                Glide.with(holder.imageView.context)
                    .load(recipes[position].imageUrl)

                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {

                            holder.progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBar.visibility = View.GONE
                            return false
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

                    .error(R.drawable.default_recipe)
            }

            withContext(Dispatchers.Main) {
                glide.await().into(holder.imageView)
            }


        }

        setCardMargins(position, holder)



    }

    fun removeRecipe(recipe: Recipe, position: Int) {

        Log.d("RV", "Current position $position")
        Log.d("RV", "Size ${recipes.size}")
        recipes.remove(recipe)
        notifyItemRemoved(position)

        if (recipes.size != 1) notifyItemRangeChanged(position - 1, recipes.size)
        else notifyItemRangeChanged(position, recipes.size)
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

            if (newState) onFavouritesClick.onAddClick(recipes[position], position, this)
            else onFavouritesClick.onDeleteClick(recipes[position], position, this)
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

    override fun getItemViewType(position: Int): Int {
        return if (recipes[position].isFavourite) R.layout.recipe_favourite_item
        else R.layout.recipe_general_item
    }

}