package com.example.recipes_book.viewModels

import com.example.recipes_book.models.retrofit.Recipe
import java.lang.StringBuilder


class ShareRecipeHandler {

    companion object {

        fun prepareShare(recipe: Recipe): String {

            val builder = StringBuilder(recipe.title).apply {

                append("\n\n")
                append("You will need:")
                append(recipe.extendedIngredients)
                append("For detailed instructions follow ")
                append(recipe.sourceUrl)
            }

            return builder.toString()
        }
    }
}