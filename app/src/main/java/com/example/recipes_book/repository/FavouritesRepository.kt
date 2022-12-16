package com.example.recipes_book.repository

import com.example.recipes_book.models.room.Recipe

interface FavouritesRepository {

    suspend fun getAllFavourites(): List<Recipe>
    suspend fun insert(recipe: Recipe)
    suspend fun delete(recipe: Recipe)
    fun isInFavourites(id: Int): Int
    suspend fun deleteAll()
}