package com.example.recipes_book.data.room

import androidx.room.*
import com.example.recipes_book.models.room.Recipe

@Dao
interface FavouritesDao {

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavourites(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Query("SELECT COUNT(*) FROM favorites WHERE id=:id")
    suspend fun isInFavourites(id: Int): Int
}