package com.example.recipes_book.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipes_book.R
import com.example.recipes_book.adapters.viewHolders.ProductViewHolder
import com.example.recipes_book.models.retrofit.ExtendedIngredient

class ProductAdapter(private val products: List<ExtendedIngredient>):
    RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.product.text = products[position].original
    }

    override fun getItemCount(): Int = products.size
}