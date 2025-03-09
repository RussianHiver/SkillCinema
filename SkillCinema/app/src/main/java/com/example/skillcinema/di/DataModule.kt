package com.example.skillcinema.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.skillcinema.data.App
import com.example.skillcinema.data.LocalRepository
import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.data.MovieDataSourceRetrofit
import com.example.skillcinema.data.MovieDatabase
import com.example.skillcinema.data.MovieTableDAO
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    fun provideMainRepository(movieDataSourceRetrofit: MovieDataSourceRetrofit): MainRepository {
        return MainRepository(movieDataSourceRetrofit)
    }

    @Provides
    fun provideMovieDataSourceRetrofit(): MovieDataSourceRetrofit {
        return MovieDataSourceRetrofit()
    }

    @Provides
    @Singleton
    fun provideApp(): App {
        return App()
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, "db").build()
    }

    @Provides
    @Singleton
    fun provideMovieTableDao(database: MovieDatabase): MovieTableDAO {
        return database.movieTableDao()
    }

    @Provides
    fun provideLocalRepository(movieTableDAO: MovieTableDAO): LocalRepository {
        return LocalRepository(movieTableDAO)
    }

}