package com.example.recipes_book.models.retrofit

data class SearchResult(
    val number: Int,
    val offset: Int,
    val results: List<SearchRecipe>,
    val totalResults: Int
)