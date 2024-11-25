package com.example.skillcinema.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.Filtered
import com.example.skillcinema.data.Gallery
import com.example.skillcinema.data.PagingSourceFiltered
import com.example.skillcinema.data.PagingSourceGallery
import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.PagingSourcePopular
import com.example.skillcinema.data.PagingSourceSearchResult
import com.example.skillcinema.data.SearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {

    private var popularType = "TOP_POPULAR_ALL"
    private var filteredGenre = "боевик"
    private var filteredCountry = "США"

    val filmId: MutableStateFlow<Int> = MutableStateFlow(263531)
    val imagesType: MutableStateFlow<String> = MutableStateFlow("STILL")

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

    var pagingDataSearchResult: Flow<PagingData<SearchResult.Item>>? = null

    fun getNewPagerForSearchResult(keyword: String) {
        pagingDataSearchResult = Pager(config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { PagingSourceSearchResult(keyword) }).flow.cachedIn(
            viewModelScope
        )
    }

    val pagingDataPopular: Flow<PagingData<Popular.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSourcePopular(popularType) }
    ).flow.cachedIn(viewModelScope)

    val pagingDataFiltered: Flow<PagingData<Filtered.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSourceFiltered(filteredGenre, filteredCountry) }
    ).flow.cachedIn(viewModelScope)

    val pagingDataGallery: Flow<PagingData<Gallery.Item>> = Pager(
        config = PagingConfig(pageSize = 20),
        pagingSourceFactory = { PagingSourceGallery(filmId.value, imagesType.value) }
    ).flow.cachedIn(viewModelScope)


}