package com.example.skillcinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.skillcinema.data.Filtered
import com.example.skillcinema.data.PagingSourceFiltered
import com.example.skillcinema.data.Popular
import com.example.skillcinema.data.PagingSourcePopular
import kotlinx.coroutines.flow.Flow

class MainViewModel: ViewModel() {

    private var popularType = "TOP_POPULAR_ALL"
    private var filteredGenre = "боевик"
    private var filteredCountry = "США"
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

    val pagingDataPopular: Flow<PagingData<Popular.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSourcePopular(popularType) }
    ).flow.cachedIn(viewModelScope)

    val pagingDataFiltered: Flow<PagingData<Filtered.Item>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { PagingSourceFiltered(filteredGenre, filteredCountry) }
    ).flow.cachedIn(viewModelScope)
}