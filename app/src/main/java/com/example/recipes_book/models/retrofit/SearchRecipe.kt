package com.example.recipes_book.models.retrofit

import android.os.Parcelable
import com.example.recipes_book.models.room.Recipe
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchRecipe(
    val id: Int,
    val image: String,
    val imageType: String,
    val title: String
) : Parcelable {

    fun toRecipe(isFavourite:Boolean = false): Recipe {
        return Recipe(
            this.id,
            this.title,
            this.image,
            isFavourite
        )
    }

}

