package com.example.skillcinema.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "film_entity")
data class FilmEntity(
    @PrimaryKey
    @ColumnInfo(name = "film_id")
    val id: Int,
    @ColumnInfo(name = "poster")
    val posterUrl: String?,
    @ColumnInfo(name = "name")
    val nameRu: String?,
    @ColumnInfo(name = "rating")
    val rating: Double?,
    @ColumnInfo(name = "genre")
    val genre: String
    )