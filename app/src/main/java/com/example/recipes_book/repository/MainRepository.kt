package com.example.recipes_book.repository

import com.example.recipes_book.models.retrofit.Result
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.retrofit.Recipe
import retrofit2.Response

interface MainRepository {

    suspend fun getRandomRecipes(): Response<Result>
    suspend fun searchRecipe(query: String, amount: Int): Response<SearchResult>
    suspend fun getRecipeById(id: Int): Response<Recipe>
}