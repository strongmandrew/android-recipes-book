package com.example.domain.useCases

import com.example.domain.models.Recipe
import com.example.domain.repository.RecipeRepository

class SearchRecipeByNameUseCase(private val repository: RecipeRepository) {

    operator fun invoke(query: String?): List<Recipe> {

        var result: List<Recipe> = listOf()

        query?.let {
            result = repository.searchRecipesByName(it)
        }

        return result

    }
}