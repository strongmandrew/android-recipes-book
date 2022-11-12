package com.example.recipes_book.repository

import com.example.recipes_book.models.retrofit.Result
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.room.Recipe
import retrofit2.Response

interface MainRepository {

    suspend fun getRandomRecipes(): Response<Result>
    suspend fun searchRecipe(query: String): Response<SearchResult>
}