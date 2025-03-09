package com.example.skillcinema.di

import android.content.Context
import com.example.skillcinema.presentation.DataViewModelFactory
import com.example.skillcinema.presentation.MainViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DataModule::class,
        DomainModule::class,
        PresentationModule::class
    ]
)
interface AppComponent {

    fun mainViewModelFactory(): MainViewModelFactory

    fun dataViewModelFactory(): DataViewModelFactory


    @Component.Builder
    interface Builder {
        fun build(): AppComponent

        @BindsInstance
        fun bindContext(context: Context): Builder
    }

}