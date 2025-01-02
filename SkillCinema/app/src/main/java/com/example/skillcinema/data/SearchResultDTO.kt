package com.example.skillcinema.data


import com.example.skillcinema.entity.Movie
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResultDTO(
    @Json(name = "films")
    val films: List<Film>,
    @Json(name = "keyword")
    val keyword: String,
    @Json(name = "pagesCount")
    val pagesCount: Int,
    @Json(name = "searchFilmsCountResult")
    val searchFilmsCountResult: Int
) {
    @JsonClass(generateAdapter = true)
    data class Film(
        @Json(name = "countries")
        val countries: List<Country>,
        @Json(name = "description")
        val description: String?,
        @Json(name = "filmId")
        override val kinopoiskId: Int,
        @Json(name = "filmLength")
        val filmLength: String?,
        @Json(name = "genres")
        val genres: List<Genre>,
        @Json(name = "nameEn")
        override val nameEn: String?,
        @Json(name = "nameRu")
        override val nameRu: String?,
        @Json(name = "posterUrl")
        override val posterUrl: String,
        @Json(name = "posterUrlPreview")
        override val posterUrlPreview: String,
        @Json(name = "rating")
        val rating: String?,
        @Json(name = "ratingVoteCount")
        val ratingVoteCount: Int?,
        @Json(name = "type")
        val type: String,
        @Json(name = "year")
        override val year: String
    ): Movie<String> {
        @JsonClass(generateAdapter = true)
        data class Country(
            @Json(name = "country")
            val country: String
        )

        @JsonClass(generateAdapter = true)
        data class Genre(
            @Json(name = "genre")
            val genre: String
        )
    }
}