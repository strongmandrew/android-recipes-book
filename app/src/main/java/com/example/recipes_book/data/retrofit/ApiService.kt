package com.example.recipes_book.data.retrofit

import com.example.recipes_book.models.retrofit.ApiResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("complexSearch")
    suspend fun getRandomRecipes(@Query("apiKey") apiKey: String, @Query("number") number: Int): Response<List<ApiResult>>

}