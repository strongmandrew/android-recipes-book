package com.example.recipes_book.data.retrofit

import com.example.recipes_book.models.retrofit.SearchResult
import com.example.recipes_book.models.retrofit.Result
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("random")
    suspend fun getRandomRecipes(@Query("apiKey") apiKey: String,
                                 @Query("number") number: Int)
    : Response<Result>

    @GET("complexSearch")
    suspend fun searchRecipes(@Query("apiKey") apiKey: String,
                                 @Query("number") number: Int,
                                 @Query("query") query: String)
    : Response<SearchResult>

}