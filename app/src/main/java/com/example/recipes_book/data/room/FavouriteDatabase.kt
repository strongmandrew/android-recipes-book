package com.example.recipes_book.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class FavouriteDatabase: RoomDatabase() {
    abstract fun getFavouritesDao(): FavouritesDao

    companion object {

        private var database: FavouriteDatabase? = null

        fun getInstance(context: Context): FavouriteDatabase {

            if (database == null) {
                database = Room
                    .databaseBuilder(context, FavouriteDatabase::class.java, "favourites")
                    .build()
            }

            return database as FavouriteDatabase
        }

    }
}