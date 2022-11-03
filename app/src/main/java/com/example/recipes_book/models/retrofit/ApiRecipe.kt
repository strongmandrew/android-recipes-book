package com.example.recipes_book.models.retrofit

import com.example.recipes_book.models.room.Recipe

data class ApiRecipe(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
) {

    fun toRecipe(): Recipe {
        return Recipe(
            this.id,
            this.title,
            this.image,
            false
        )
    }

}

