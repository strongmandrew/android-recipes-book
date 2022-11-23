package com.example.recipes_book.models.room

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites")
data class Recipe(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String?,

    @ColumnInfo(name = "isFavourite")
    var isFavourite: Boolean

) : Parcelable
