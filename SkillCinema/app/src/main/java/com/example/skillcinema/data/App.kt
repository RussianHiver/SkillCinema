package com.example.skillcinema.data

import android.app.Application
import androidx.room.Room

class App : Application() {

    lateinit var db: MovieDatabase

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "db"
        ).build()

    }

}