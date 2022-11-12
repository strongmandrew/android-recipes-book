package com.example.recipes_book.data

import com.example.recipes_book.data.retrofit.RetrofitInstance
import com.example.recipes_book.models.retrofit.Result
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.repository.MainRepository
import retrofit2.Response

const val API_KEY = "70254bc4318e40dcb6344aad63e456ec"
const val MAX_AMOUNT = 100

class MainRepositoryImpl: MainRepository {

    private val apiService by lazy {
        RetrofitInstance.api
    }

    override suspend fun getRandomRecipes(): Response<Result> {
        return apiService.getRandomRecipes(API_KEY, MAX_AMOUNT)
    }

    override suspend fun searchRecipe(query: String): Response<SearchResult> {
        return apiService.searchRecipe(API_KEY, MAX_AMOUNT, query)
    }
}