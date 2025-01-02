package com.example.skillcinema.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.skillcinema.data.FilteredDTO
import com.example.skillcinema.data.GenresAndCountriesDTO
import com.example.skillcinema.data.MainRepository
import com.example.skillcinema.di.DaggerAppComponent
import javax.inject.Inject

class FilteredMovieUseCase @Inject constructor(private val mainRepository: MainRepository) {

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

    fun getMovieFilteredPager(
        filteredCountry: String,
        filteredGenre: String
    ): Pager<Int, FilteredDTO.Item> {
        val pagingSourceFiltered = DaggerAppComponent.create().pagingSourceFiltered()
        pagingSourceFiltered.inputGenreAndCountryType(filteredCountry, filteredGenre)
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { pagingSourceFiltered }
        )
    }

}