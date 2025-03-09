package com.example.skillcinema.data

import android.app.Application
import com.example.skillcinema.di.AppComponent
import com.example.skillcinema.di.DaggerAppComponent

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerAppComponent.builder().bindContext(this).build()
    }

}