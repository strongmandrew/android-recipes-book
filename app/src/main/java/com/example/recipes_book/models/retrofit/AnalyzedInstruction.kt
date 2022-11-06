package com.example.recipes_book.models.retrofit

data class AnalyzedInstruction(
    val name: String,
    val steps: List<Step>
)