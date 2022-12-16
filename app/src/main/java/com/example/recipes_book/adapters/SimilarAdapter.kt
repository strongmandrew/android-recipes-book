package com.example.recipes_book.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.recipes_book.R
import com.example.recipes_book.adapters.viewHolders.SimilarViewHolder
import com.example.recipes_book.models.retrofit.SearchRecipe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SimilarAdapter(private val similars: List<SearchRecipe>,
                     private val listener: OnSimilarClickListener):
    RecyclerView.Adapter<SimilarViewHolder>() {

    interface OnSimilarClickListener {
        fun onSimilarClick(similar: SearchRecipe)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarViewHolder {
        val view =LayoutInflater.from(parent.context)
            .inflate(R.layout.similar_item, parent, false)

        return SimilarViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimilarViewHolder, position: Int) {
        holder.title.text = similars[position].title

        CoroutineScope(Dispatchers.IO).launch {

            val glide = Glide.with(holder.itemView)
                .load(similars[position].image)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.default_recipe)
                .error(R.drawable.default_recipe)

            withContext(Dispatchers.Main) {
                glide.into(holder.image)
            }
        }

        holder.card.setOnClickListener {
            listener.onSimilarClick(similars[position])
        }
    }

    override fun getItemCount(): Int = similars.size
}