package com.example.skillcinema.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.GalleryDTO
import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.data.MovieDataDTO
import com.example.skillcinema.data.PopularDTO
import com.example.skillcinema.data.PremieresDTO
import com.example.skillcinema.data.SearchResultDTO
import com.example.skillcinema.data.SimilarsDTO
import com.example.skillcinema.data.StaffListDTO
import com.example.skillcinema.data.StaffMemberDTO
import com.example.skillcinema.domain.FilteredMovieUseCase
import com.example.skillcinema.domain.FiltersUseCase
import com.example.skillcinema.domain.MovieGalleryUseCase
import com.example.skillcinema.domain.MovieStaffUseCase
import com.example.skillcinema.domain.OneMovieUseCase
import com.example.skillcinema.domain.PopularMovieUseCase
import com.example.skillcinema.domain.PremierMovieUseCase
import com.example.skillcinema.domain.SearchMovieUseCase
import com.example.skillcinema.domain.SimilarMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val premierMovieUseCase: PremierMovieUseCase,
    private val popularMovieUseCase: PopularMovieUseCase,
    private val filteredMovieUseCase: FilteredMovieUseCase,
    private val oneMovieUseCase: OneMovieUseCase,
    private val movieStaffUseCase: MovieStaffUseCase,
    private val movieGalleryUseCase: MovieGalleryUseCase,
    private val similarMoviesUseCase: SimilarMoviesUseCase,
    private val filtersUseCase: FiltersUseCase,
    private val searchMovieUseCase: SearchMovieUseCase
) : ViewModel() {

    private var popularType = "TOP_POPULAR_ALL"
    private var filteredGenre = "боевик"
    private var filteredCountry = "США"

    val filmId: MutableStateFlow<Int> = MutableStateFlow(263531)

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
        popularType = when (type) {
            "TOP_250_MOVIES" -> type
            "TOP_250_TV_SHOWS" -> type
            else -> "TOP_POPULAR_ALL"
        }
    }

    fun chooseFilteredGenre(genre: String) {
        filteredGenre = when (genre) {
            "драма" -> genre
            else -> "боевик"
        }
    }

    fun chooseFilteredCountry(country: String) {
        filteredCountry = when (country) {
            "Франция" -> country
            else -> "США"
        }
    }

    val pagingDataPopular: Flow<PagingData<PopularDTO.Item>> =
        popularMovieUseCase.getMoviesPopularPager(popularType).flow.cachedIn(viewModelScope)

    val pagingDataFiltered: Flow<PagingData<FilteredDTO.Item>> =
        filteredMovieUseCase.getMovieFilteredPager(filteredCountry, filteredGenre).flow.cachedIn(
            viewModelScope
        )

    val pagingDataGalleryStill: Flow<PagingData<GalleryDTO.Item>> =
        movieGalleryUseCase.getMovieGalleryPager(
            filmId.value,
            "STILL"
        ).flow.cachedIn(viewModelScope)

    val pagingDataGalleryShooting: Flow<PagingData<GalleryDTO.Item>> =
        movieGalleryUseCase.getMovieGalleryPager(
            filmId.value,
            "SHOOTING"
        ).flow.cachedIn(viewModelScope)

    val pagingDataGalleryPoster: Flow<PagingData<GalleryDTO.Item>> =
        movieGalleryUseCase.getMovieGalleryPager(
            filmId.value,
            "POSTER"
        ).flow.cachedIn(viewModelScope)


    val pagingDataSearchResult = MutableStateFlow<PagingData<SearchResultDTO.Film>?>(null)

    suspend fun getNewPagerForSearchResult(keyword: String) {
        searchMovieUseCase.getSearchResultPager(keyword).flow.collectLatest { pagingData ->
            pagingDataSearchResult.value = pagingData
        }
    }

}