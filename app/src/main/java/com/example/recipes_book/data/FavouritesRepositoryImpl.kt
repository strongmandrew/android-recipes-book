package com.example.recipes_book.data

import android.content.Context
import com.example.recipes_book.data.room.FavouriteDatabase
import com.example.recipes_book.models.room.Recipe
import com.example.recipes_book.repository.FavouritesRepository

class FavouritesRepositoryImpl(context: Context): FavouritesRepository {

    private val dao by lazy {
        FavouriteDatabase.getInstance(context).getFavouritesDao()
    }

    override suspend fun getAllFavourites(): List<Recipe> {
        return dao.getAllFavourites()
    }

    override suspend fun insert(recipe: Recipe) {
        dao.insert(recipe)
    }

    override suspend fun delete(recipe: Recipe) {
        dao.delete(recipe)
    }

    override suspend fun isInFavourites(recipe: Recipe): Int {
        return dao.isInFavourites(recipe.id)
    }
}