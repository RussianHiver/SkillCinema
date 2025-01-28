package com.example.skillcinema.di

import com.example.skillcinema.presentation.MainViewModelFactory
import com.example.skillcinema.presentation.pagingsources.PagingSourceFiltered
import com.example.skillcinema.presentation.pagingsources.PagingSourceGallery
import com.example.skillcinema.presentation.pagingsources.PagingSourcePopular
import com.example.skillcinema.presentation.pagingsources.PagingSourceSearchResult
import dagger.Component

@Component
interface AppComponent {

    fun mainViewModelFactory(): MainViewModelFactory

}