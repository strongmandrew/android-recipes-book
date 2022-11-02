package com.example.recipes_book.models

data class ApiResult(
    val number: Int,
    val offset: Int,
    val results: List<ApiRecipe>,
    val totalResults: Int
)