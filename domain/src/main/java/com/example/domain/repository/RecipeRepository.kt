package com.example.domain.repository

import com.example.domain.models.Recipe

interface RecipeRepository {

    fun getRandomRecipes(amount: Int): List<Recipe>
    fun searchRecipesByName(query: String?): List<Recipe>
}