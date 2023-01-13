package com.example.recipes_book.data

import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.models.retrofit.Result
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.repository.MainRepository
import com.example.recipes_book.models.retrofit.Recipe
import retrofit2.Response

const val API_KEY = "70254bc4318e40dcb6344aad63e456ec"
const val RANDOM_AMOUNT = 100

class MainRepositoryImpl: MainRepository {

    private val apiService by lazy {
        RetrofitInstance.api
    }

    override suspend fun getRecipeById(id: Int): Response<Recipe> {
        return apiService.getRecipeById(id, API_KEY)
    }

    override suspend fun getRandomRecipes(): Response<Result> {
        return apiService.getRandomRecipes(API_KEY, RANDOM_AMOUNT)
    }

        override suspend fun searchRecipe(query: String, amount: Int): Response<SearchResult> {
        return apiService.searchRecipe(API_KEY, amount, query)
    }
}