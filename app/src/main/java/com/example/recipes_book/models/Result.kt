package com.example.recipes_book.models

data class Result(
    val number: Int,
    val offset: Int,
    val results: List<Recipe>,
    val totalResults: Int
)