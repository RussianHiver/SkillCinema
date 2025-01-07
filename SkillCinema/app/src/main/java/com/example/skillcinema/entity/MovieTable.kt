package com.example.skillcinema.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieTable(
    @ColumnInfo
    @PrimaryKey
    val id: Int
)
