package com.example.recipes_book.data.retrofit

import com.example.recipes_book.models.retrofit.Recipe
import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.retrofit.Result
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("random")
    suspend fun getRandomRecipes(@Query("apiKey") apiKey: String,
                                 @Query("number") number: Int)
    : Response<Result>

    @GET("complexSearch")
    suspend fun searchRecipe(@Query("apiKey") apiKey: String,
                             @Query("number") number: Int,
                             @Query("query") query: String)
    : Response<SearchResult>

    @GET("{id}/information")
    suspend fun getRecipeById(@Path("id") id: Int,
                              @Query("apiKey") apiKey: String)
    : Response<Recipe>

}