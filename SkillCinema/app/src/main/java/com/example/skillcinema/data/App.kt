package com.example.skillcinema.data

import android.app.Application
import androidx.room.Room
import com.example.skillcinema.di.AppComponent
import com.example.skillcinema.di.DaggerAppComponent

class App : Application() {

    lateinit var db: MovieDatabase
    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "db"
        ).build()

        component = DaggerAppComponent.create()
    }

}