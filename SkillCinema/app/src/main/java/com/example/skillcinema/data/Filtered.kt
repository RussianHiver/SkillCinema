package com.example.skillcinema.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Filtered(
    @Json(name = "items") val items: List<Item>,
    @Json(name = "total") val total: Int,
    @Json(name = "totalPages") val totalPages: Int
) {
    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "countries") val countries: List<Country>,
        @Json(name = "genres") val genres: List<Genre>,
        @Json(name = "imdbId") val imdbId: String?,
        @Json(name = "kinopoiskId") val kinopoiskId: Int,
        @Json(name = "nameEn") val nameEn: String?,
        @Json(name = "nameOriginal") val nameOriginal: String?,
        @Json(name = "nameRu")  val nameRu: String?,
        @Json(name = "posterUrl") val posterUrl: String,
        @Json(name = "posterUrlPreview") val posterUrlPreview: String,
        @Json(name = "ratingImdb") val ratingImdb: Double?,
        @Json(name = "ratingKinopoisk") val ratingKinopoisk: Double?,
        @Json(name = "type") val type: String,
        @Json(name = "year") val year: Int?
    ) {
        @JsonClass(generateAdapter = true)
        data class Country(
            @Json(name = "country") val country: String
        )

        @JsonClass(generateAdapter = true)
        data class Genre(
            @Json(name = "genre")  val genre: String
        )
    }
}