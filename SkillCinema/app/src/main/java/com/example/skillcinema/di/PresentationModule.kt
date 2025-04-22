package com.example.skillcinema.di

import com.example.skillcinema.domain.DatabaseCollectionsUseCase
import com.example.skillcinema.domain.FilteredMovieUseCase
import com.example.skillcinema.domain.FiltersUseCase
import com.example.skillcinema.domain.MovieGalleryUseCase
import com.example.skillcinema.domain.MovieStaffUseCase
import com.example.skillcinema.domain.OneMovieUseCase
import com.example.skillcinema.domain.PopularMovieUseCase
import com.example.skillcinema.domain.PremierMovieUseCase
import com.example.skillcinema.domain.SeasonsAndEpisodesUseCase
import com.example.skillcinema.domain.SimilarMoviesUseCase
import com.example.skillcinema.domain.DatabaseFilmsUseCase
import com.example.skillcinema.presentation.DataViewModel
import com.example.skillcinema.presentation.DataViewModelFactory
import com.example.skillcinema.presentation.MainViewModel
import com.example.skillcinema.presentation.MainViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class PresentationModule {

    @Provides
    fun provideViewModel(
        filteredMovieUseCase: FilteredMovieUseCase,
        filtersUseCase: FiltersUseCase,
        movieGalleryUseCase: MovieGalleryUseCase,
        movieStaffUseCase: MovieStaffUseCase,
        oneMovieUseCase: OneMovieUseCase,
        popularMovieUseCase: PopularMovieUseCase,
        premierMovieUseCase: PremierMovieUseCase,
        seasonsAndEpisodesUseCase: SeasonsAndEpisodesUseCase,
        similarMoviesUseCase: SimilarMoviesUseCase,
        databaseFilmsUseCase: DatabaseFilmsUseCase,
        databaseCollectionsUseCase: DatabaseCollectionsUseCase
    ): MainViewModel {
        return MainViewModel(
            filteredMovieUseCase = filteredMovieUseCase,
            filtersUseCase = filtersUseCase,
            movieGalleryUseCase = movieGalleryUseCase,
            movieStaffUseCase = movieStaffUseCase,
            oneMovieUseCase = oneMovieUseCase,
            popularMovieUseCase = popularMovieUseCase,
            premierMovieUseCase = premierMovieUseCase,
            seasonsAndEpisodesUseCase = seasonsAndEpisodesUseCase,
            similarMoviesUseCase = similarMoviesUseCase,
            databaseFilmsUseCase = databaseFilmsUseCase,
            databaseCollectionsUseCase = databaseCollectionsUseCase
        )
    }

    @Provides
    fun provideViewModelFactory(mainViewModel: MainViewModel): MainViewModelFactory {
        return MainViewModelFactory(mainViewModel)
    }

    @Provides
    fun provideDataViewModel(): DataViewModel {
        return DataViewModel()
    }

    @Provides
    fun provideDataViewModelFactory(dataViewModel: DataViewModel): DataViewModelFactory{
        return DataViewModelFactory(dataViewModel)
    }

}