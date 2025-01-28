package com.example.skillcinema.data

import javax.inject.Inject

class MainRepository @Inject constructor(
    private val movieDataSource: MovieDataSourceRetrofit
) {

    suspend fun getMoviesPremiers(): PremieresDTO {
        return movieDataSource.searchDataApi.getMoviePremieres()
    }

    suspend fun getMoviesPopular(page: Int, type: String): PopularDTO {
        return movieDataSource.searchDataApi.getPopular(page = page, type = type)
    }

    suspend fun getFilteredMovies(page: Int, genre: Array<Int>, country: Array<Int>): FilteredDTO {
        return movieDataSource.searchDataApi.getFilteredMovies(
            page = page,
            genres = genre,
            countries = country
        )
    }

    suspend fun getFilters(): GenresAndCountriesDTO {
        return movieDataSource.searchDataApi.getFilters()
    }

    suspend fun getOneMovie(id: Int): MovieDataDTO {
        return movieDataSource.searchDataApi.getOneMovie(id)
    }

    suspend fun getMovieStaff(id: Int): List<StaffListDTO> {
        return movieDataSource.searchDataApi.getFilmStaff(filmId = id)
    }

    suspend fun getGalleryPhotos(page: Int, id: Int, type: String): GalleryDTO {
        return movieDataSource.searchDataApi.getFilmGallery(
            id = id,
            page = page,
            type = type
        )
    }

    suspend fun getSimilarMovies(id: Int): SimilarsDTO {
        return movieDataSource.searchDataApi.getSimilars(id = id)
    }

    suspend fun getIndividualStaffMember(id: Int): StaffMemberDTO {
        return movieDataSource.searchDataApi.getIndividualStaffMember(id = id)
    }

    suspend fun getSearchResult(keyword: String, page: Int): SearchResultDTO {
        return movieDataSource.searchDataApi.getSearchResult(keyword = keyword, page = page)
    }

    suspend fun getSeasonsAndEpisodes(id: Int): SeasonsAndEpisodesDTO {
        return movieDataSource.searchDataApi.getSeasonsAndEpisodes(id)
    }

}