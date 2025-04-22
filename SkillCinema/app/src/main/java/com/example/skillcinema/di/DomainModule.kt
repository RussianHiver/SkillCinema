package com.example.skillcinema.di

import com.example.skillcinema.data.LocalRepository
import com.example.skillcinema.data.MainRepository
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
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideFilteredMovieUseCase(mainRepository: MainRepository): FilteredMovieUseCase {
        return FilteredMovieUseCase(mainRepository)
    }

    @Provides
    fun provideFiltersUseCase(mainRepository: MainRepository): FiltersUseCase {
        return FiltersUseCase(mainRepository)
    }

    @Provides
    fun provideMovieGalleryUseCase(mainRepository: MainRepository): MovieGalleryUseCase {
        return MovieGalleryUseCase(mainRepository)
    }

    @Provides
    fun provideMovieStaffUseCase(mainRepository: MainRepository): MovieStaffUseCase {
        return MovieStaffUseCase(mainRepository)
    }

    @Provides
    fun provideOneMovieUseCase(mainRepository: MainRepository): OneMovieUseCase {
        return OneMovieUseCase(mainRepository)
    }

    @Provides
    fun providePopularMovieUseCase(mainRepository: MainRepository): PopularMovieUseCase {
        return PopularMovieUseCase(mainRepository)
    }

    @Provides
    fun providePremierMovieUseCase(mainRepository: MainRepository): PremierMovieUseCase {
        return PremierMovieUseCase(mainRepository)
    }

    @Provides
    fun provideSeasonsAndEpisodesUseCase(mainRepository: MainRepository): SeasonsAndEpisodesUseCase {
        return SeasonsAndEpisodesUseCase(mainRepository)
    }

    @Provides
    fun provideSimilarMoviesUseCase(mainRepository: MainRepository): SimilarMoviesUseCase {
        return SimilarMoviesUseCase(mainRepository)
    }

    @Provides
    fun provideDatabaseFilmsUseCase(localRepository: LocalRepository): DatabaseFilmsUseCase {
        return DatabaseFilmsUseCase(localRepository)
    }

    @Provides
    fun provideDatabaseCollectionsUseCase(localRepository: LocalRepository): DatabaseCollectionsUseCase {
        return DatabaseCollectionsUseCase(localRepository)
    }

}