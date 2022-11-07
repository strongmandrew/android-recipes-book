package com.example.recipes_book.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipes_book.models.room.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun getFavouritesDao(): FavouritesDao

    companion object {

        @Volatile
        private var database: FavouriteDatabase? = null

        @Synchronized
        fun getInstance(context: Context): FavouriteDatabase {

            return if (database == null) {
                database = Room
                    .databaseBuilder(
                        context,
                        FavouriteDatabase::class.java,
                        "favourites")
                    .build()

                database as FavouriteDatabase
            }
            else {
                database as FavouriteDatabase
            }
        }

    }
}