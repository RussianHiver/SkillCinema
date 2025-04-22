package com.example.skillcinema.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.data.PremieresDTO
import com.example.skillcinema.data.SeasonsAndEpisodesDTO
import com.example.skillcinema.data.SimilarsDTO
import com.example.skillcinema.data.StaffListDTO
import com.example.skillcinema.data.StaffMemberDTO
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
import com.example.skillcinema.entity.CollectionEntity
import com.example.skillcinema.entity.CollectionListFilms
import com.example.skillcinema.entity.FilmEntity
import com.example.skillcinema.presentation.pagingsources.PagingSourceFiltered
import com.example.skillcinema.presentation.pagingsources.PagingSourceGallery
import com.example.skillcinema.presentation.pagingsources.PagingSourcePopular
import com.example.skillcinema.presentation.pagingsources.PagingSourceSearchResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val premierMovieUseCase: PremierMovieUseCase,
    private val popularMovieUseCase: PopularMovieUseCase,
    private val filteredMovieUseCase: FilteredMovieUseCase,
    private val oneMovieUseCase: OneMovieUseCase,
    private val movieStaffUseCase: MovieStaffUseCase,
    private val movieGalleryUseCase: MovieGalleryUseCase,
    private val similarMoviesUseCase: SimilarMoviesUseCase,
    private val filtersUseCase: FiltersUseCase,
    private val seasonsAndEpisodesUseCase: SeasonsAndEpisodesUseCase,
    private val databaseFilmsUseCase: DatabaseFilmsUseCase,
    private val databaseCollectionsUseCase: DatabaseCollectionsUseCase
) : ViewModel() {

    private var popularType = MutableStateFlow("")
    private var filteredGenre = MutableStateFlow("")
    private var filteredCountry = MutableStateFlow("")

    val filmId: MutableStateFlow<Int> = MutableStateFlow(0)

    suspend fun givePremierList(): List<PremieresDTO.Item> {
        return premierMovieUseCase.getMoviesPremiers().items
    }

    suspend fun givePopularList(page: Int, type: String): List<PopularDTO.Item> {
        return popularMovieUseCase.getMoviesPopular(page = page, type = type).items
    }

    suspend fun giveFilteredList(
        page: Int,
        country: String,
        genre: String
    ): List<FilteredDTO.Item> {
        return filteredMovieUseCase.getMoviesFiltered(
            page = page,
            country = country,
            genre = genre
        ).items
    }

    suspend fun giveOneMovie(id: Int): MovieDataDTO {
        return oneMovieUseCase.getOneMovie(id)
    }

    suspend fun giveMovieStaffList(id: Int): List<StaffListDTO> {
        return movieStaffUseCase.getMovieStaffList(id = id)
    }

    suspend fun giveFilmGalleryList(id: Int, page: Int, type: String): List<GalleryDTO.Item> {
        return movieGalleryUseCase.getMovieGallery(id = id, page = page, type = type).items
    }

    suspend fun giveSimilars(id: Int): SimilarsDTO {
        return similarMoviesUseCase.getSimilars(id = id)
    }

    suspend fun giveFilters(): GenresAndCountriesDTO {
        return filtersUseCase.getFilters()
    }

    suspend fun giveIndividualStaffMember(id: Int): StaffMemberDTO {
        return movieStaffUseCase.getIndividualStaffMember(id = id)
    }

    fun choosePopularType(type: String) {
        popularType.value = type
    }

    fun putCountryAndGenre(genre: String, country: String) {
        filteredCountry.value = country
        filteredGenre.value = genre
    }

    fun insertCollection(collection: CollectionEntity) {
        viewModelScope.launch {
            databaseCollectionsUseCase.insertCollection(collection)
        }
    }

    fun deleteFilmForCollection(film: CollectionListFilms) {
        viewModelScope.launch {
            databaseCollectionsUseCase.deleteFilmForCollection(film)
        }
    }

    fun insertFilm(film: FilmEntity) {
        viewModelScope.launch {
            databaseFilmsUseCase.insertFilm(film)
        }
    }

    fun insertFilmForCollection(filmCollection: CollectionListFilms) {
        viewModelScope.launch {
            databaseCollectionsUseCase.insertFilmForCollection(filmCollection)
        }
    }

    fun deleteCollection(collection: CollectionEntity) {
        viewModelScope.launch {
            databaseCollectionsUseCase.deleteCollection(collection)
        }

    }

    fun checkTheFilm(filmId: Int, collectionID: Int): CollectionListFilms {
        return databaseFilmsUseCase.checkTheFilm(filmId, collectionID)
    }

    val allCollection = databaseCollectionsUseCase.getAllCollections()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(3000L),
            initialValue = emptyList()  //пустой список
        )

    val pagingDataPopular: Flow<PagingData<PopularDTO.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            PagingSourcePopular(
                popularType.value,
                popularMovieUseCase = popularMovieUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    val pagingDataFiltered: Flow<PagingData<FilteredDTO.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            PagingSourceFiltered(
                country = filteredCountry.value,
                genre = filteredGenre.value,
                filteredMovieUseCase = filteredMovieUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    val pagingDataGalleryStill: Flow<PagingData<GalleryDTO.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            PagingSourceGallery(
                filmId = filmId.value,
                type = "STILL",
                movieGalleryUseCase = movieGalleryUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    val pagingDataGalleryShooting: Flow<PagingData<GalleryDTO.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            PagingSourceGallery(
                filmId = filmId.value,
                type = "SHOOTING",
                movieGalleryUseCase = movieGalleryUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)

    val pagingDataGalleryPoster: Flow<PagingData<GalleryDTO.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            PagingSourceGallery(
                filmId = filmId.value,
                type = "POSTER",
                movieGalleryUseCase = movieGalleryUseCase
            )
        }
    ).flow.cachedIn(viewModelScope)


    val pagingDataSearchResult = MutableStateFlow<PagingData<FilteredDTO.Item>?>(null)

    suspend fun getNewPagerForSearchResult(
        keyword: String,
        country: String,
        genre: String,
        type: String,
        order: String,
        ratingFrom: Float,
        ratingTo: Float,
        yearFrom: Int,
        yearTo: Int
    ) {
        Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
                PagingSourceSearchResult(
                    keyword = keyword,
                    country = country,
                    genre = genre,
                    type = type,
                    order = order,
                    ratingFrom = ratingFrom,
                    ratingTo = ratingTo,
                    yearFrom = yearFrom,
                    yearTo = yearTo,
                    filteredMovieUseCase = filteredMovieUseCase
                )
            }
        ).flow.collectLatest { pagingData ->
            pagingDataSearchResult.value = pagingData
        }
    }

    suspend fun getSeasonsAndEpisodes(id: Int): SeasonsAndEpisodesDTO {
        return seasonsAndEpisodesUseCase.getSeasonsAndEpisodes(id)
    }
}