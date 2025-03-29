package com.example.skillcinema.domain

import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.data.MainRepository

class FilteredMovieUseCase(private val mainRepository: MainRepository) {

    suspend fun getMoviesFiltered(
        page: Int,
        country: String,
        genre: String
    ): FilteredDTO {
        val filters = mainRepository.getFilters()
        val neededCountry = getCountryByFilter(country, filters)
        val neededGenre = getGenreByFilter(genre, filters)
        return mainRepository.getFilteredMovies(
            page = page,
            country = arrayOf(neededCountry),
            genre = arrayOf(neededGenre)
        )
    }

    suspend fun getSearchResultMovies(
        page: Int,
        country: String,
        genre: String,
        keyword: String,
        type: String,
        order: String,
        ratingFrom: Float,
        ratingTo: Float,
        yearFrom: Int,
        yearTo: Int
    ): FilteredDTO {
        val filters = mainRepository.getFilters()
        val neededCountry = getCountryByFilter(country, filters)
        val neededGenre = getGenreByFilter(genre, filters)
        return mainRepository.getSearchResult(
            page = page,
            country = arrayOf(neededCountry),
            genre = arrayOf(neededGenre),
            keyword = keyword,
            order = order,
            type = type,
            ratingFrom = ratingFrom,
            ratingTo = ratingTo,
            yearFrom = yearFrom,
            yearTo = yearTo
        )
    }

    private fun getGenreByFilter(
        genre: String,
        filters: GenresAndCountriesDTO
    ): Int {
        val neededGenre: List<GenresAndCountriesDTO.Genre> =
            filters.genres.filter { it.genre == genre }
        return neededGenre.first().id
    }

    private fun getCountryByFilter(
        country: String,
        filters: GenresAndCountriesDTO
    ): Int {
        val neededCountry: List<GenresAndCountriesDTO.Country> =
            filters.countries.filter { it.country == country }
        return neededCountry.first().id
    }
}