package com.example.domain.useCases

import com.example.domain.models.Recipe
import com.example.domain.repository.RecipeRepository

class GetRandomRecipesUseCase(private val repository: RecipeRepository) {

    operator fun invoke(amount: Int): List<Recipe> {

        return repository.getRandomRecipes(amount)
    }

}