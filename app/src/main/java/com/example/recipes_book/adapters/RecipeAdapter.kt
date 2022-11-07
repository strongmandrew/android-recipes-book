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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "RecipeAdapter"

class RecipeAdapter(private val onFavouritesClick: FavouritesClickListener):
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    var recipes = listOf<Recipe>()
    set(value) {
        val callback = RecipeDiffCallback(recipes, value)
        val diff = DiffUtil.calculateDiff(callback)
        diff.dispatchUpdatesTo(this)
        field = value
    }

    interface FavouritesClickListener {
        fun onAddClick(recipe: Recipe)
        fun onDeleteClick(recipe: Recipe)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card: CardView = itemView.findViewById(R.id.recipe_card_item)
        val titleField: TextView = itemView.findViewById(R.id.title_text)
        val favouritesButton: CheckBox = itemView.findViewById(R.id.favorites_btn)
        val imageView: ShapeableImageView = itemView.findViewById(R.id.image_view)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d(TAG, "onCreateViewHolder call")

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder call")
        holder.titleField.text = recipes[position].title

        setupFavouritesButton(holder, position)

        var imageBitmap: Bitmap? = holder.imageView.drawable.toBitmap()

        imageBitmap = setupImageLoader(holder, position, imageBitmap)

        setCardMargins(position, holder)

        setCardBackground(imageBitmap, holder)

    }

    private fun setupImageLoader(
        holder: ViewHolder,
        position: Int,
        imageBitmap: Bitmap?
    ): Bitmap? {
        var imageBitmap1 = imageBitmap
        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(holder.itemView)
                .load(recipes[position].imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
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

                        imageBitmap1 = resource?.toBitmap()

                        return false
                    }
                })
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imageView)

        }
        return imageBitmap1
    }

    private fun setupFavouritesButton(
        holder: ViewHolder,
        position: Int
    ) {
        holder.favouritesButton.isChecked = recipes[position].isFavourite


        holder.favouritesButton.setOnClickListener {
            val newState = !recipes[position].isFavourite
            recipes[position].isFavourite = newState
            holder.favouritesButton.isChecked = newState

            if (newState) onFavouritesClick.onAddClick(recipes[position])
            else onFavouritesClick.onDeleteClick(recipes[position])
        }
    }

    private fun setCardMargins(
        position: Int,
        holder: ViewHolder
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

    private fun setCardBackground(
        imageBitmap: Bitmap?,
        holder: ViewHolder
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            val palette = Palette.from(imageBitmap!!).generate()

            val mutedColor = palette.mutedSwatch

            holder.card.setCardBackgroundColor(mutedColor?.rgb ?: "#FFFFFF".toColorInt())
        }
    }

    override fun getItemCount(): Int = recipes.size

}