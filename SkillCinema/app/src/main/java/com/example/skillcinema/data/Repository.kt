package com.example.skillcinema.data

class Repository {

    suspend fun getMoviesPopular(page: Int, type: String): Popular {
        return RetrofitInstance.searchDataApi.getPopular(page = page, type = type)
    }

    suspend fun getFilteredMovies(page: Int, genre: String, country: String): Filtered {

        return RetrofitInstance.searchDataApi.getFilteredMovies(
            page = page,
            genres = arrayOf(getGenreByFilter(genre)),
            countries = arrayOf(getCountryByFilter(country))
        )
    }

    suspend fun getGalleryPhotos(page: Int, id: Int, type: String): Gallery {
        return RetrofitInstance.searchDataApi.getFilmGallery(
            id = id,
            page = page,
            type = type
        )
    }

    suspend fun getGenreByFilter(genre: String): Int {
        val filters = RetrofitInstance.searchDataApi.getFilters()
        val neededGenre: List<GenresAndCountries.Genre> =
            filters.genres.filter { it.genre == genre }
        return if (genre == "боевик") 1 else neededGenre.first().id
    }

    suspend fun getCountryByFilter(country: String): Int {
        val filters = RetrofitInstance.searchDataApi.getFilters()
        val neededCountry: List<GenresAndCountries.Country> =
            filters.countries.filter { it.country == country }
        return if (country == "США") 1 else neededCountry.first().id
    }
}