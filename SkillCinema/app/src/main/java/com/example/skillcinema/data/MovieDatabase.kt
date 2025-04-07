package com.example.skillcinema.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.FilmEntity

@Database(
    entities = [FilmEntity::class, CollectionEntity::class, CollectionListFilms::class],
    version = 1
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieTableDao(): MovieTableDAO

}