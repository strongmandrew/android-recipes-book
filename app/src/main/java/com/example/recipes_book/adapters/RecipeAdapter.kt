package com.example.recipes_book.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toColorInt
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.recipes_book.R
import com.example.recipes_book.models.room.Recipe
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.*

const val TAG = "RecipeAdapter"

class RecipeAdapter(private val onFavouritesClick: FavouritesClickListener):
    ListAdapter<Recipe, RecipeViewHolder>(RecipeItemCallback()) {

    interface FavouritesClickListener {
        fun onAddClick(recipe: Recipe)
        fun onDeleteClick(recipe: Recipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        Log.d(TAG, "onCreateViewHolder call")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder call")
        holder.titleField.text = getItem(position).title

        setupFavouritesButton(holder, position)

        var imageBitmap: Bitmap? = holder.imageView.drawable.toBitmap()

        val loadImageScope = CoroutineScope(Dispatchers.Main).async {
            Glide.with(holder.itemView)
                .load(getItem(position).imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {

                        holder.imageView.visibility = ImageView.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        imageBitmap = resource?.toBitmap()

                        return false
                    }
                })
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView)

        }

        setCardMargins(position, holder)

        CoroutineScope(Dispatchers.IO).launch {

            loadImageScope.await()

            val palette = Palette.from(imageBitmap!!).generate()

            val mutedColor = palette.mutedSwatch

            withContext(Dispatchers.Main) {

                holder.card.setCardBackgroundColor(mutedColor?.rgb ?: "#FFFFFF".toColorInt())
            }

        }


    }

    private fun setupFavouritesButton(
        holder: RecipeViewHolder,
        position: Int
    ) {
        holder.favouritesButton.isChecked = getItem(position).isFavourite


        holder.favouritesButton.setOnClickListener {
            val newState = !getItem(position).isFavourite
            getItem(position).isFavourite = newState
            holder.favouritesButton.isChecked = newState

            if (newState) onFavouritesClick.onAddClick(getItem(position))
            else onFavouritesClick.onDeleteClick(getItem(position))
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
            currentList.size - 1 -> params.setMargins(40, 10, 40, 220)
            else -> params.setMargins(40, 10, 40, 10)
        }

        holder.card.layoutParams = params
    }

}