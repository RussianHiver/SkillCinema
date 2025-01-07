package com.example.skillcinema.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.skillcinema.entity.MovieTable

@Database(entities = [MovieTable::class], version = 1)
abstract class MovieDatabase: RoomDatabase() {

    abstract fun movieTableDao(): MovieTableDAO

}